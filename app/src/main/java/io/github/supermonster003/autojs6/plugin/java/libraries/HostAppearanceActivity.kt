package io.github.supermonster003.autojs6.plugin.java.libraries

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager

/**
 * A page in AutoJs6's appearance ([HostAppearance]): the language and night mode through the base context, the
 * theme colors on the action bar, the status bar and the section headings. Asks the host again on every resume (back from
 * AutoJs6 after a change of its settings) and recreates the page when the answer differs and the page was not touched.
 * zh-CN: 使用 AutoJs6 外观 ([HostAppearance]) 的页面: 语言与夜间模式经 base context 生效, 主题色用于操作栏, 状态栏与各节标题.
 * 每次恢复时 (例如在 AutoJs6 改设置后返回) 重新询问宿主, 应答不同且页面未被操作时重建页面.
 */
abstract class HostAppearanceActivity : Activity() {

    internal lateinit var appearance: HostAppearance
        private set
    private var generation = 0
    private var interacted = false

    override fun attachBaseContext(newBase: Context) {
        appearance = HostAppearance.load(newBase)
        super.attachBaseContext(appearance.wrap(newBase))
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appearance.primary?.let { actionBar?.setBackgroundDrawable(ColorDrawable(it)) }
        appearance.primaryDark?.let { color ->
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
            val flags = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            val light = AppearanceColors.foregroundOn(color) == AppearanceColors.BLACK
            window.decorView.systemUiVisibility = if (light) flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else flags
        }
    }

    override fun onTitleChanged(title: CharSequence?, color: Int) {
        super.onTitleChanged(title, color)
        val background = appearance.primary ?: return
        val foreground = AppearanceColors.foregroundOn(background)
        actionBar?.title = SpannableString(title ?: "").apply {
            setSpan(ForegroundColorSpan(foreground), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    /** Shows the up arrow in the foreground color of the host's action bar. zh-CN: 以宿主操作栏的前景色显示向上箭头. */
    protected fun showUpArrow() {
        val bar = actionBar ?: return
        bar.setDisplayHomeAsUpEnabled(true)
        val background = appearance.primary ?: return
        val attributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.homeAsUpIndicator))
        val arrow = try {
            attributes.getDrawable(0)
        } finally {
            attributes.recycle()
        }
        arrow?.mutate()?.setTint(AppearanceColors.foregroundOn(background))
        bar.setHomeAsUpIndicator(arrow)
    }

    /**
     * The host's accent made readable on the page (the section headings), null when the page follows the system.
     * zh-CN: 调整为在页面上可读的宿主强调色 (用于各节标题), 页面跟随系统时为 null.
     */
    protected fun readableAccent(): Int? {
        val accent = appearance.accent ?: return null
        return AppearanceColors.readable(accent, themeColor(android.R.attr.colorBackground, AppearanceColors.WHITE))
    }

    /**
     * The paragraph direction of the page language, for the root view: a Latin library name or version heading then sits
     * on the right in Arabic like the lines around it. [View.TEXT_DIRECTION_LOCALE] would follow the system language
     * instead of the language of the page (AutoJs6's).
     * zh-CN: 页面语言的段落方向 (用于根视图): 阿拉伯语下拉丁文库名或版本标题与周围各行一样靠右. TEXT_DIRECTION_LOCALE 跟随的是系统语言,
     * 而非页面 (AutoJs6) 的语言.
     */
    protected fun pageTextDirection(): Int = when (resources.configuration.layoutDirection) {
        View.LAYOUT_DIRECTION_RTL -> View.TEXT_DIRECTION_RTL
        else -> View.TEXT_DIRECTION_FIRST_STRONG
    }

    protected fun themeColor(attribute: Int, fallback: Int): Int {
        val attributes = theme.obtainStyledAttributes(intArrayOf(attribute))
        return try {
            attributes.getColor(0, fallback)
        } finally {
            attributes.recycle()
        }
    }

    override fun onResume() {
        super.onResume()
        interacted = false
        val current = ++generation
        HostAppearance.refresh(this) { host ->
            runOnUiThread {
                if (current != generation || isFinishing || isDestroyed) return@runOnUiThread
                if (!interacted && host != appearance) recreate()
            }
        }
    }

    override fun onUserInteraction() {
        interacted = true
        super.onUserInteraction()
    }

    override fun onPause() {
        generation++
        super.onPause()
    }

}
