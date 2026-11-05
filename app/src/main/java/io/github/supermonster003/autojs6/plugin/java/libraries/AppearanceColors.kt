package io.github.supermonster003.autojs6.plugin.java.libraries

import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Color arithmetic for the host's theme colors (WCAG relative luminance and contrast), free of Android types for the
 * JVM tests: pale host colors such as AutoJs6's default navajo white stay visible on light and dark pages.
 * zh-CN: 宿主主题色的颜色运算 (WCAG 相对亮度与对比度), 不依赖 Android 类型以便 JVM 测试: 让 AutoJs6 默认的纳瓦白等浅色在亮暗页面上都可见.
 */
internal object AppearanceColors {

    const val BLACK = -0x1000000
    const val WHITE = -0x1

    fun luminance(color: Int): Double {
        fun channel(shift: Int): Double {
            val value = (color shr shift and 0xff) / 255.0
            return if (value <= 0.03928) value / 12.92 else ((value + 0.055) / 1.055).pow(2.4)
        }
        return 0.2126 * channel(16) + 0.7152 * channel(8) + 0.0722 * channel(0)
    }

    fun contrast(first: Int, second: Int): Double {
        val (lighter, darker) = listOf(luminance(first), luminance(second)).sortedDescending()
        return (lighter + 0.05) / (darker + 0.05)
    }

    /** Black or white, whichever reads better on [background]. zh-CN: 黑或白中在 [background] 上更易读的一个. */
    fun foregroundOn(background: Int) = if (contrast(BLACK, background) >= contrast(WHITE, background)) BLACK else WHITE

    /**
     * [color] moved towards black (light [background]) or white (dark) in steps of 5 % until the contrast reaches
     * [minimum], the rule of the AutoJs4 Runtime settings page.
     * zh-CN: 把 [color] 以 5 % 的步长向黑 (亮色 [background]) 或白 (暗色) 靠拢, 直到对比度达到 [minimum], 同 AutoJs4 Runtime 设置页的规则.
     */
    fun readable(color: Int, background: Int, minimum: Double = 3.0): Int {
        val target = if (luminance(background) > 0.5) BLACK else WHITE
        var result = color
        for (step in 0..20) {
            result = blend(color, target, step / 20f)
            if (contrast(result, background) >= minimum) break
        }
        return result
    }

    fun withAlpha(color: Int, alpha: Int) = (color and 0xffffff) or (alpha shl 24)

    private fun blend(from: Int, to: Int, ratio: Float): Int {
        fun mix(shift: Int) = ((from shr shift and 0xff) * (1 - ratio) + (to shr shift and 0xff) * ratio).roundToInt() shl shift
        return mix(24) or mix(16) or mix(8) or mix(0)
    }

}
