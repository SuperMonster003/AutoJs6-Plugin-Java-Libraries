package io.github.supermonster003.autojs6.plugin.java.libraries

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.apache.commons.lang3.time.DateFormatUtils
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayInputStream
import java.util.TimeZone

/**
 * A smoke test of the exported libraries with the versions the APK contains (ROADMAP.md P0.5, appendix C): the
 * representative classes load, the example of the launch page prints what its comments say, and each library does a little
 * real work (Jackson reading its annotations). The release APK keeps the same classes under their original names
 * (`verifyReleaseDexClasses`); AutoJs6 runs them on devices (host `device_java_packages_check.py`).
 * zh-CN: 以 APK 所含版本对导出库做冒烟测试 (ROADMAP.md P0.5, 附录 C): 代表类能加载, 启动页示例的输出与其注释一致, 各库做一点实际工作
 * (Jackson 读取注解). release APK 以原名保留相同的类 (verifyReleaseDexClasses); 设备上由 AutoJs6 运行 (宿主 device_java_packages_check.py).
 */
class JavaLibrariesSmokeTest {

    @get:Rule
    val folder = TemporaryFolder()

    @Test
    fun theRepresentativeClassesOfAppendixCLoad() {
        APPENDIX_C.forEach { name -> assertEquals(name, Class.forName(name, false, javaClass.classLoader).name) }
    }

    @Test
    fun theExampleOfTheLaunchPagePrintsWhatItsCommentsSay() {
        val input = ByteArrayInputStream("hello".toByteArray(Charsets.UTF_8))
        val mapper = ObjectMapper()
        val tree = mapper.readTree("""{"name": "AutoJs6", "version": 6}""")
        val printed = listOf(
            StringUtils.isBlank("  ").toString(),
            StringUtils.capitalize("autojs6"),
            IOUtils.toString(input, "UTF-8"),
            tree.get("version").asInt().toString(),
            mapper.writeValueAsString(tree),
        )
        val comments = JavaLibraries.EXAMPLE.lines().mapNotNull { COMMENT.matchEntire(it)?.groupValues?.get(1) }
        assertEquals(comments, printed)
    }

    @Test
    fun commonsLangFormatsDatesAndBuildsStrings() {
        assertEquals("1970-01-01 00:00", DateFormatUtils.format(0L, "yyyy-MM-dd HH:mm", TimeZone.getTimeZone("UTC")))
        assertEquals("AutoJs6,6", ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("name", "AutoJs6").append("version", 6).toString())
    }

    @Test
    fun commonsIoWritesReadsAndNamesFiles() {
        val file = folder.root.resolve("scripts/main.js")
        FileUtils.writeStringToFile(file, "toast('AutoJs6');", Charsets.UTF_8)
        assertEquals("toast('AutoJs6');", FileUtils.readFileToString(file, Charsets.UTF_8))
        assertEquals("js", FilenameUtils.getExtension(file.path))
        assertEquals("main", FilenameUtils.getBaseName(file.path))
    }

    @Test
    fun jacksonWritesAndReadsAnnotatedClassesAndStreams() {
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(Release().apply { name = "1.0.0"; code = 9 })
        assertEquals(mapper.readTree("""{"version_name": "1.0.0", "version_code": 9}"""), mapper.readTree(json))
        val release = mapper.readValue("""{"version_name": "6.8.1", "version_code": 6640}""", Release::class.java)
        assertEquals("6.8.1" to 6640, release.name to release.code)
        val tokens = JsonFactory().createParser("[1, 2]").use { parser -> generateSequence { parser.nextToken() }.toList() }
        assertEquals(listOf(JsonToken.START_ARRAY, JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NUMBER_INT, JsonToken.END_ARRAY), tokens)
    }

    class Release {
        @JvmField
        @field:JsonProperty("version_name")
        var name: String = ""

        @JvmField
        @field:JsonProperty("version_code")
        var code: Int = 0
    }

    private companion object {

        val APPENDIX_C = listOf(
            "org.apache.commons.lang3.StringUtils",
            "org.apache.commons.lang3.time.DateFormatUtils",
            "org.apache.commons.lang3.builder.ToStringBuilder",
            "org.apache.commons.io.IOUtils",
            "org.apache.commons.io.FileUtils",
            "org.apache.commons.io.FilenameUtils",
            "com.fasterxml.jackson.databind.ObjectMapper",
            "com.fasterxml.jackson.databind.JsonNode",
            "com.fasterxml.jackson.core.JsonFactory",
            "com.fasterxml.jackson.annotation.JsonProperty",
        )

        val COMMENT = Regex("""^console\.log\(.+\); // (.+)$""")

    }

}
