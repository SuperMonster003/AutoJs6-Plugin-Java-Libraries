package io.github.supermonster003.autojs6.plugin.java.libraries

import org.autojs.plugin.common.api.AutoJs6HostSettingsContract as Contract
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HostAppearanceTest {

    private val answer = mapOf<String, Any?>(
        Contract.KEY_PROTOCOL_VERSION to Contract.PROTOCOL_VERSION,
        Contract.KEY_HOST_PACKAGE_NAME to Contract.HOST_PACKAGE_NAME,
        Contract.KEY_HOST_VERSION_CODE to 6640L,
        Contract.KEY_RESOLVED_LANGUAGE_TAG to "zh-Hant-HK",
        Contract.KEY_DARK_MODE_ACTIVE to true,
        Contract.KEY_THEME_COLOR_PRIMARY to 0xffdead,
        Contract.KEY_THEME_COLOR_PRIMARY_DARK to 0xffc87a,
        Contract.KEY_THEME_COLOR_ACCENT to 0x7f2196f3,
    )

    private fun decode(changes: Map<String, Any?> = emptyMap()) = (answer + changes).let { HostAppearance.decode(it::get) }

    @Test
    fun anAnswerOfTheContractGivesTheHostAppearanceWithOpaqueColors() {
        assertEquals(HostAppearance("zh-Hant-HK", true, 0xffffdead.toInt(), 0xffffc87a.toInt(), 0xff2196f3.toInt()), decode())
    }

    @Test
    fun anAnswerThatIsNotUnderstoodFollowsTheSystem() {
        listOf(
            mapOf(Contract.KEY_PROTOCOL_VERSION to 2),
            mapOf(Contract.KEY_HOST_PACKAGE_NAME to "org.autojs.autojs6.other"),
            mapOf(Contract.KEY_RESOLVED_LANGUAGE_TAG to null),
            mapOf(Contract.KEY_RESOLVED_LANGUAGE_TAG to "zh_CN"),
            mapOf(Contract.KEY_RESOLVED_LANGUAGE_TAG to ""),
            mapOf(Contract.KEY_DARK_MODE_ACTIVE to "true"),
            mapOf(Contract.KEY_THEME_COLOR_ACCENT to null),
            mapOf(Contract.KEY_THEME_COLOR_PRIMARY to 0xffdeadL),
        ).forEach { assertNull(it.toString(), decode(it)) }
    }

    @Test
    fun paleHostColorsStayReadableOnLightPages() {
        val navajoWhite = 0xffffdead.toInt()
        assertEquals(AppearanceColors.BLACK, AppearanceColors.foregroundOn(navajoWhite))
        assertEquals(AppearanceColors.WHITE, AppearanceColors.foregroundOn(0xff212121.toInt()))
        assertTrue(AppearanceColors.contrast(AppearanceColors.readable(navajoWhite, 0xfffafafa.toInt()), 0xfffafafa.toInt()) >= 3.0)
        assertEquals(navajoWhite, AppearanceColors.readable(navajoWhite, 0xff303030.toInt()))
        assertEquals(21.0, AppearanceColors.contrast(AppearanceColors.BLACK, AppearanceColors.WHITE), 0.001)
    }

}
