package io.github.supermonster003.autojs6.plugin.java.libraries

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/**
 * The launch page (ROADMAP.md D12): the libraries with their versions, license and Java package, an example of their use
 * in scripts, their NOTICE files and the release history, in AutoJs6's appearance. There is nothing to set: the plugin is
 * switched on and off in Plugin Center.
 * zh-CN: 启动页 (ROADMAP.md D12): 各库的版本, 许可证与 Java 包, 在脚本中使用的示例, NOTICE 文件与发行历史, 使用 AutoJs6 的外观.
 * 没有设置项: 插件在插件中心开关.
 */
class SettingsActivity : HostAppearanceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(8), dp(16), dp(16))
        }
        content.addView(text(getString(R.string.text_settings_summary), 14f, secondary = true))

        content.addView(heading(R.string.text_group_libraries))
        JavaLibraries.ALL.forEach { content.addView(row(it)) }

        content.addView(heading(R.string.text_group_usage))
        content.addView(text(getString(R.string.text_usage_summary), 14f, secondary = true))
        content.addView(code(JavaLibraries.EXAMPLE, 12f), LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply { topMargin = dp(8) })

        content.addView(heading(R.string.text_group_notices))
        JavaLibraries.ALL.forEach { library ->
            val versions = library.artifacts.map { it.version }.distinct().joinToString(", ")
            content.addView(text("${library.name} $versions", 14f).apply { setPadding(0, dp(8), 0, dp(4)) })
            content.addView(code(library.notice, 11f), MATCH_PARENT, WRAP_CONTENT)
        }

        content.addView(heading(R.string.text_release_history).apply {
            setPadding(0, dp(20), 0, dp(12))
            setBackgroundResource(TypedValue().also { theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true) }.resourceId)
            setOnClickListener { startActivity(Intent(this@SettingsActivity, ReleaseHistoryActivity::class.java)) }
        }, MATCH_PARENT, WRAP_CONTENT)

        val scroll = ScrollView(this).apply {
            // Code and NOTICE files keep left to right. zh-CN: 代码与 NOTICE 文件保持从左到右.
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

    private fun row(library: Library): View = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(0, dp(8), 0, dp(8))
        addView(text(library.name, 16f), MATCH_PARENT, WRAP_CONTENT)
        val versions = library.artifacts.singleOrNull()?.let { getString(R.string.text_library_version, it.version) }
            ?: getString(R.string.text_library_artifacts, library.artifacts.joinToString(", ") { "${it.module.substringAfter(':')} ${it.version}" })
        val details = listOf(
            versions,
            getString(R.string.text_library_license, library.license),
            getString(R.string.text_library_package, library.exportedPackage),
        )
        addView(text(details.joinToString("\n"), 13f, secondary = true), MATCH_PARENT, WRAP_CONTENT)
    }

    private fun heading(title: Int) = text(getString(title), 16f).apply {
        setPadding(0, dp(20), 0, dp(4))
        setTypeface(typeface, Typeface.BOLD)
        readableAccent()?.let(::setTextColor)
    }

    /**
     * Monospaced text kept left to right (code and NOTICE files read the same in an RTL language), selectable, scrolling
     * sideways instead of wrapping.
     * zh-CN: 等宽文本, 保持从左到右 (代码与 NOTICE 文件在 RTL 语言下也照原样显示), 可选择, 横向滚动而不换行.
     */
    private fun code(value: String, sp: Float): View {
        val block = TextView(this).apply {
            text = value
            typeface = Typeface.MONOSPACE
            setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
            setTextIsSelectable(true)
            textDirection = View.TEXT_DIRECTION_LTR
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            layoutDirection = View.LAYOUT_DIRECTION_LTR
            setPadding(dp(12), dp(10), dp(12), dp(10))
        }
        return HorizontalScrollView(this).apply {
            layoutDirection = View.LAYOUT_DIRECTION_LTR
            isFillViewport = true
            background = GradientDrawable().apply {
                cornerRadius = dp(6).toFloat()
                setColor(AppearanceColors.withAlpha(themeColor(android.R.attr.textColorPrimary, AppearanceColors.BLACK), 16))
            }
            addView(block, MATCH_PARENT, WRAP_CONTENT)
        }
    }

    private fun text(value: String, sp: Float, secondary: Boolean = false) = TextView(this).apply {
        text = value
        setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
        if (secondary) alpha = 0.7f
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

}
