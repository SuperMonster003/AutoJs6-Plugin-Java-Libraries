package io.github.supermonster003.autojs6.plugin.java.libraries

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import org.autojs.plugin.common.api.IPluginInfoProvider
import org.autojs.plugin.common.api.PluginCapabilityKeys
import org.autojs.plugin.common.api.PluginInfo

/**
 * Answers `org.autojs.plugin.INFO` (category `java-libraries`) for the Plugin Center list. Scripts never reach this
 * service: AutoJs6 loads the exported classes from the APK.
 * zh-CN: 为插件中心列表响应 `org.autojs.plugin.INFO` (category `java-libraries`). 脚本不会用到此服务: AutoJs6 直接从 APK 加载导出的类.
 */
class JavaLibrariesInfoService : Service() {

    private val binder = object : IPluginInfoProvider.Stub() {
        override fun getInfo(): PluginInfo = pluginInfo()
    }

    override fun onBind(intent: Intent?): IBinder = binder

}

internal fun Context.pluginInfo(): PluginInfo {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return PluginInfo().apply {
        name = getString(R.string.app_name)
        description = getString(R.string.plugin_description)
        instruction = JavaLibrariesContract.INSTRUCTION_REFERENCE
        author = "SuperMonster003"
        versionName = packageInfo.versionName.orEmpty()
        versionCode = packageInfo.versionCodeCompat()
        versionDate = BuildConfig.VERSION_DATE
        id = JavaLibrariesContract.PLUGIN_ID
        engine = JavaLibrariesContract.ENGINE
        supportedAbis = emptyArray()
        capabilities = Bundle().apply {
            putLong(PluginCapabilityKeys.REQUIRES_HOST_VERSION, BuildConfig.REQUIRED_HOST_VERSION_CODE)
        }
    }
}

private fun PackageInfo.versionCodeCompat(): Long {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) return longVersionCode
    @Suppress("DEPRECATION")
    return versionCode.toLong()
}
