package io.github.supermonster003.autojs6.plugin.java.libraries

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/**
 * Release history of the launch page: the bundled changelog of the current language (AutoJs6's, see
 * [HostAppearanceActivity]), English when there is none.
 * zh-CN: 启动页的发行历史: 当前语言 (AutoJs6 的语言, 见 [HostAppearanceActivity]) 的内置更新日志, 没有时使用英语.
 */
class ReleaseHistoryActivity : HostAppearanceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.text_release_history)
        showUpArrow()
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(8), dp(16), dp(16))
        }
        val lines = load()?.let(ReleaseHistory::parse).orEmpty()
        if (lines.isEmpty()) content.addView(text(getString(R.string.text_release_history_load_failed), 14f))
        lines.forEach { line ->
            content.addView(
                when (line.kind) {
                    ReleaseHistory.Kind.VERSION -> text(line.text, 18f, Typeface.BOLD).apply { setPadding(0, dp(16), 0, 0) }
                    ReleaseHistory.Kind.DATE -> text(line.text, 13f).apply { alpha = 0.7f }
                    ReleaseHistory.Kind.ITEM -> text(item(line), 14f).apply { setPadding(0, dp(6), 0, 0) }
                    ReleaseHistory.Kind.TEXT -> text(line.text, 14f)
                },
            )
        }
        val scroll = ScrollView(this).apply {
            textDirection = pageTextDirection()
            addView(content, MATCH_PARENT, WRAP_CONTENT)
        }
        scroll.setOnApplyWindowInsetsListener { view, insets ->
            @Suppress("DEPRECATION")
            view.setPadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
            insets
        }
        setContentView(scroll)
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun load(): String? = ReleaseHistory.candidates(resources.configuration.locales[0]).firstNotNullOfOrNull { path ->
        runCatching { assets.open(path).bufferedReader(Charsets.UTF_8).use { it.readText() } }.getOrNull()?.takeIf(String::isNotBlank)
    }

    private fun item(line: ReleaseHistory.Line): CharSequence = SpannableStringBuilder("- ").apply {
        line.label?.let { label ->
            append(label, StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            append("  ")
        }
        append(line.text)
    }

    private fun text(value: CharSequence, sp: Float, style: Int = Typeface.NORMAL) = TextView(this).apply {
        text = value
        setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
        if (style != Typeface.NORMAL) setTypeface(typeface, style)
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

}
