package io.github.supermonster003.autojs6.plugin.java.libraries

import android.app.Activity
import android.os.Bundle

/**
 * The wake entry of the plugin contract (`org.autojs.plugin.WAKE_ACTIVITY`): AutoJs6 starts it to bring the package out
 * of the stopped state; it only finishes.
 * zh-CN: 插件契约的唤醒入口 (`org.autojs.plugin.WAKE_ACTIVITY`): AutoJs6 启动它以解除包的停止状态; 它只负责结束自身.
 */
class WakeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}
