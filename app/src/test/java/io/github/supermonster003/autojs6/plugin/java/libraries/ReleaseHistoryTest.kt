package io.github.supermonster003.autojs6.plugin.java.libraries

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.util.Locale

class ReleaseHistoryTest {

    private val assets = File("src/main/assets")

    @Test
    fun everyLanguageOfThePluginReadsItsOwnChangelog() {
        val expected = mapOf(
            Locale.forLanguageTag("zh-CN") to "zh-Hans",
            Locale.forLanguageTag("zh-Hans-SG") to "zh-Hans",
            Locale.forLanguageTag("zh-HK") to "zh-Hant-HK",
            Locale.forLanguageTag("zh-MO") to "zh-Hant-HK",
            Locale.forLanguageTag("zh-TW") to "zh-Hant-TW",
            Locale.forLanguageTag("zh-Hant") to "zh-Hant-TW",
            Locale.forLanguageTag("en-US") to "en",
            Locale.forLanguageTag("fr-CA") to "fr",
            Locale.forLanguageTag("es-MX") to "es",
            Locale.forLanguageTag("ja-JP") to "ja",
            Locale.forLanguageTag("ko-KR") to "ko",
            Locale.forLanguageTag("ru-RU") to "ru",
            Locale.forLanguageTag("ar-EG") to "ar",
        )
        expected.forEach { (locale, tag) ->
            val candidates = ReleaseHistory.candidates(locale)
            assertEquals(locale.toLanguageTag(), "doc/CHANGELOG-$tag.md", candidates.first())
            assertTrue(candidates.all { File(assets, it).isFile })
        }
    }

    @Test
    fun otherLanguagesFallBackToEnglishThenTheDefaultCopy() {
        assertEquals(
            listOf("doc/CHANGELOG-en.md", "doc/CHANGELOG.md"),
            ReleaseHistory.candidates(Locale.forLanguageTag("de-DE")),
        )
    }

    @Test
    fun theGeneratedChangelogParsesIntoVersionsDatesAndLabelledItems() {
        val lines = ReleaseHistory.parse(File(assets, "doc/CHANGELOG-en.md").readText())
        assertEquals(ReleaseHistory.Line(ReleaseHistory.Kind.VERSION, "v1.0.0"), lines.first())
        assertEquals(ReleaseHistory.Kind.DATE, lines[1].kind)
        assertTrue(Regex("""\d{4}/\d{2}/\d{2}""").matches(lines[1].text))
        val items = lines.filter { it.kind == ReleaseHistory.Kind.ITEM }
        assertTrue(items.isNotEmpty())
        assertTrue(items.all { it.label in setOf("Hint", "Feature", "Fix", "Improvement", "Dependency") })
        assertTrue(lines.none { it.kind == ReleaseHistory.Kind.TEXT })
    }

    @Test
    fun itemsWithoutALabelAndStrayTextAreKept() {
        val lines = ReleaseHistory.parse("# v2.0.0\n\n###### 2026/01/02\n\n* plain item\nnote\n******\n### Release history\n")
        assertEquals(
            listOf(
                ReleaseHistory.Line(ReleaseHistory.Kind.VERSION, "v2.0.0"),
                ReleaseHistory.Line(ReleaseHistory.Kind.DATE, "2026/01/02"),
                ReleaseHistory.Line(ReleaseHistory.Kind.ITEM, "plain item"),
                ReleaseHistory.Line(ReleaseHistory.Kind.TEXT, "note"),
            ),
            lines,
        )
    }

}
