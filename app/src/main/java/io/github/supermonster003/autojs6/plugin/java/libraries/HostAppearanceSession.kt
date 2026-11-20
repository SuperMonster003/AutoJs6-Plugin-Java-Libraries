package io.github.supermonster003.autojs6.plugin.java.libraries

import android.app.Activity
import android.content.Context

/**
 * AutoJs6's appearance ([HostAppearance]) for one activity, whatever its base class: the language and night mode through
 * the base context ([attach]), and a fresh answer on every resume that recreates the activity when it differs and the
 * page was not touched. [HostAppearanceActivity] and the Compose pages (which must extend `ComponentActivity`) share it.
 * zh-CN: 一个 Activity 使用的 AutoJs6 外观 ([HostAppearance]), 与其基类无关: 语言与夜间模式经 base context 生效 ([attach]),
 * 每次恢复时重新询问宿主, 应答不同且页面未被操作时重建. [HostAppearanceActivity] 与 Compose 页面 (必须继承 `ComponentActivity`) 共用.
 */
internal class HostAppearanceSession(private val activity: Activity) {

    lateinit var appearance: HostAppearance
        private set
    private var generation = 0
    private var interacted = false

    /** Call from `attachBaseContext`: the base context in the host's language and night mode. zh-CN: 在 `attachBaseContext` 中调用. */
    fun attach(newBase: Context): Context {
        appearance = HostAppearance.load(newBase)
        return appearance.wrap(newBase)
    }

    fun onResume() {
        interacted = false
        val current = ++generation
        HostAppearance.refresh(activity) { host ->
            activity.runOnUiThread {
                if (current != generation || activity.isFinishing || activity.isDestroyed) return@runOnUiThread
                if (!interacted && host != appearance) activity.recreate()
            }
        }
    }

    fun onUserInteraction() {
        interacted = true
    }

    fun onPause() {
        generation++
    }

}

/** A color attribute of this context's theme, [fallback] when the theme has none. zh-CN: 当前主题的颜色属性, 没有时为 [fallback]. */
internal fun Context.themeColor(attribute: Int, fallback: Int): Int {
    val attributes = theme.obtainStyledAttributes(intArrayOf(attribute))
    return try {
        attributes.getColor(0, fallback)
    } finally {
        attributes.recycle()
    }
}
