package io.github.supermonster003.autojs6.plugin.java.libraries

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import org.autojs.plugin.common.api.AutoJs6HostSettingsContract as Contract
import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * AutoJs6's appearance for the settings pages (host roadmap appendix C Q115 (2)): its language, whether it is dark right
 * now and its theme colors, read through the versioned host settings contract. Null fields follow the system, which is
 * the whole appearance when AutoJs6 is not installed, disabled or refuses the call.
 * zh-CN: 设置页使用的 AutoJs6 外观 (宿主 roadmap 附录 C Q115 (2)): 语言, 当前是否为深色与主题色, 经带版本的宿主设置契约读取.
 * 字段为 null 表示跟随系统; AutoJs6 未安装, 被停用或拒绝调用时整体跟随系统.
 */
internal data class HostAppearance(
    val languageTag: String?,
    val dark: Boolean?,
    val primary: Int?,
    val primaryDark: Int?,
    val accent: Int?,
) {

    /** [base] with the host's language and night mode applied. zh-CN: 应用了宿主语言与夜间模式的 [base]. */
    fun wrap(base: Context): Context {
        if (languageTag == null && dark == null) return base
        val configuration = Configuration(base.resources.configuration)
        languageTag?.let { tag ->
            val locale = Locale.forLanguageTag(tag)
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
        }
        dark?.let { night ->
            val mode = if (night) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
            configuration.uiMode = (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or mode
        }
        return base.createConfigurationContext(configuration)
    }

    companion object {

        val SYSTEM = HostAppearance(null, null, null, null, null)

        private const val TIMEOUT_MILLIS = 400L

        private val executor = Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, "JavaLibrariesHostAppearance").apply { isDaemon = true }
        }

        /** The last answer of the host, [SYSTEM] once it was unavailable. zh-CN: 宿主的上次应答, 宿主不可用后为 [SYSTEM]. */
        @Volatile
        private var cached: HostAppearance? = null

        /**
         * The appearance for a new page: asks the host on a worker and waits at most [TIMEOUT_MILLIS]; the last answer
         * serves when the host is slow (a cold start of AutoJs6), the system when it never answered.
         * zh-CN: 新页面的外观: 在工作线程询问宿主并至多等待 [TIMEOUT_MILLIS]; 宿主较慢 (AutoJs6 冷启动) 时使用上次的应答, 从未应答时跟随系统.
         */
        fun load(context: Context): HostAppearance {
            val application = context.applicationContext ?: context
            val future = executor.submit<HostAppearance> { query(application) }
            return runCatching { future.get(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS) }.getOrNull() ?: cached ?: SYSTEM
        }

        /** Asks the host again on a worker and passes the answer to [callback] there. zh-CN: 在工作线程重新询问宿主, 并在该线程把应答交给 [callback]. */
        fun refresh(context: Context, callback: (HostAppearance) -> Unit) {
            val application = context.applicationContext ?: context
            executor.execute { callback(query(application)) }
        }

        private fun query(context: Context): HostAppearance {
            val answer = runCatching {
                context.contentResolver.call(Uri.parse(Contract.CONTENT_URI), Contract.METHOD_GET_SETTINGS, null, null)?.let(::parse)
            }.getOrNull()
            return (answer ?: SYSTEM).also { cached = it }
        }

        private fun parse(answer: Bundle): HostAppearance? {
            if (answer.hasFileDescriptors()) return null
            @Suppress("DEPRECATION")
            return decode(answer::get)
        }

        /**
         * The appearance of a contract answer ([value] gives the value of a key); null for another protocol version, another
         * host package, a missing or mistyped field or an invalid language tag.
         * zh-CN: 契约应答对应的外观 ([value] 按键取值); 协议版本或宿主包名不符, 字段缺失或类型不符, 语言标签无效时为 null.
         */
        fun decode(value: (String) -> Any?): HostAppearance? {
            if (value(Contract.KEY_PROTOCOL_VERSION) != Contract.PROTOCOL_VERSION) return null
            if (value(Contract.KEY_HOST_PACKAGE_NAME) != Contract.HOST_PACKAGE_NAME) return null
            val tag = (value(Contract.KEY_RESOLVED_LANGUAGE_TAG) as? String)?.takeIf(::isLanguageTag) ?: return null
            fun color(key: String) = (value(key) as? Int)?.let { it or OPAQUE }
            return HostAppearance(
                languageTag = tag,
                dark = value(Contract.KEY_DARK_MODE_ACTIVE) as? Boolean ?: return null,
                primary = color(Contract.KEY_THEME_COLOR_PRIMARY) ?: return null,
                primaryDark = color(Contract.KEY_THEME_COLOR_PRIMARY_DARK) ?: return null,
                accent = color(Contract.KEY_THEME_COLOR_ACCENT) ?: return null,
            )
        }

        private fun isLanguageTag(tag: String) = tag.length in 2..80 && tag.matches(LANGUAGE_TAG) &&
            runCatching { Locale.Builder().setLanguageTag(tag).build().language.isNotEmpty() }.getOrDefault(false)

        private val LANGUAGE_TAG = Regex("[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*")

        private const val OPAQUE = -0x1000000

    }

}
