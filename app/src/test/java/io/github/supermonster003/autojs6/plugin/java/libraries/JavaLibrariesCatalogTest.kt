package io.github.supermonster003.autojs6.plugin.java.libraries

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

/**
 * The libraries agree wherever the plugin names them: the versions of gradle/libs.versions.toml (ROADMAP.md D2) on the
 * launch page and in THIRD_PARTY_NOTICES.md, the exported packages (D9, D10) in the manifest meta-data, the contract, the
 * launch page and the keep rules, the NOTICE files (D18) on the launch page and in THIRD_PARTY_NOTICES.md, and the example
 * of the launch page, which names only classes of the exported packages and of Java itself.
 * zh-CN: 插件各处提到的库彼此一致: gradle/libs.versions.toml 的版本 (ROADMAP.md D2) 与启动页, THIRD_PARTY_NOTICES.md 一致;
 * 导出包 (D9, D10) 在 Manifest meta-data, 契约, 启动页与 keep 规则中一致; NOTICE 文件 (D18) 在启动页与 THIRD_PARTY_NOTICES.md
 * 中一致; 启动页示例只引用导出包与 Java 自身的类.
 */
class JavaLibrariesCatalogTest {

    // Unit tests run in the module directory (app/). zh-CN: 单元测试在模块目录 (app/) 中运行.
    private val project = File("..")

    private val catalog = project.resolve("gradle/libs.versions.toml").readText()

    /** The `[versions]` of the version catalog: alias to version. */
    private val versions: Map<String, String> by lazy {
        section("versions").mapNotNull { VERSION.matchEntire(it)?.destructured?.let { (alias, version) -> alias to version } }.toMap()
    }

    /** The `[libraries]` of the version catalog: Maven module to version. */
    private val modules: Map<String, String> by lazy {
        section("libraries").mapNotNull { LIBRARY.find(it)?.destructured?.let { (module, alias) -> module to versions.getValue(alias) } }.toMap()
    }

    private fun section(name: String): List<String> = catalog.lines().map(String::trim)
        .dropWhile { it != "[$name]" }.drop(1)
        .takeWhile { !it.startsWith("[") }
        .filter { it.isNotEmpty() && !it.startsWith("#") }

    @Test
    fun theVersionCatalogPinsTheVersionsAutoJs6IncludedUpTo670() {
        assertEquals(D2_VERSIONS, D2_VERSIONS.keys.associateWith { modules[it] })
    }

    @Test
    fun theLaunchPageShowsTheVersionsOfTheVersionCatalog() {
        val artifacts = JavaLibraries.ALL.flatMap(Library::artifacts)
        assertEquals(D2_VERSIONS.keys.toList(), artifacts.map(Library.Artifact::module))
        artifacts.forEach { assertEquals(it.module, modules.getValue(it.module), it.version) }
        assertTrue(JavaLibraries.ALL.all { it.license == JavaLibraries.LICENSE })
    }

    @Test
    fun theExportedPackagesAgreeInTheManifestTheContractTheLaunchPageAndTheKeepRules() {
        val packages = JavaLibrariesContract.EXPORTED_PACKAGES
        assertEquals(listOf("org.apache.commons.lang3", "org.apache.commons.io", "com.fasterxml.jackson"), packages)
        assertEquals(packages.joinToString(","), metaData(JavaLibrariesContract.META_DATA_EXPORTS))
        assertEquals(packages, JavaLibraries.ALL.map(Library::exportedPackage))
        val kept = KEEP.findAll(File("proguard-rules.pro").readText()).map { it.groupValues[1] }.toList()
        assertEquals(packages, kept - "org.autojs.plugin")
    }

    @Test
    fun thirdPartyNoticesListTheVersionsOfTheVersionCatalog() {
        val rows = NOTICES_ROW.findAll(notices()).associate { it.groupValues[1] to it.groupValues[2] }
        assertEquals(D2_VERSIONS.keys.associateWith { modules.getValue(it) }, rows)
        val headings = NOTICE_HEADING.findAll(notices()).map { it.groupValues[1] }.toList()
        assertEquals(
            listOf(
                "Apache Commons Lang ${BuildConfig.COMMONS_LANG3_VERSION}",
                "Apache Commons IO ${BuildConfig.COMMONS_IO_VERSION}",
                "Jackson Databind ${BuildConfig.JACKSON_DATABIND_VERSION} and Jackson Core ${BuildConfig.JACKSON_CORE_VERSION}",
            ),
            headings,
        )
    }

    @Test
    fun theLaunchPageShowsTheNoticeFilesOfThirdPartyNotices() {
        val blocks = NOTICE_BLOCK.findAll(notices()).map { it.groupValues[1] }.toList()
        assertEquals(JavaLibraries.ALL.map(Library::notice), blocks)
    }

    @Test
    fun theExampleNamesOnlyClassesOfTheExportedPackagesAndOfJava() {
        val packages = JavaLibrariesContract.EXPORTED_PACKAGES
        val names = CLASS_NAME.findAll(JavaLibraries.EXAMPLE).map { it.value }.toList()
        names.forEach { name -> assertTrue(name, name.startsWith("java.") || packages.any { name.startsWith("$it.") }) }
        assertEquals(packages, packages.filter { prefix -> names.any { it.startsWith("$prefix.") } })
    }

    private fun notices() = project.resolve("THIRD_PARTY_NOTICES.md").readText()

    private fun metaData(name: String): String? {
        val factory = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
        val elements = factory.newDocumentBuilder().parse(File("src/main/AndroidManifest.xml")).getElementsByTagName("meta-data")
        return (0 until elements.length).map { elements.item(it) as Element }
            .single { it.getAttributeNS(ANDROID, "name") == name }
            .getAttributeNS(ANDROID, "value")
    }

    private companion object {

        const val ANDROID = "http://schemas.android.com/apk/res/android"

        // ROADMAP.md D2: the versions AutoJs6 6.7.0 included, in the order of the launch page.
        // zh-CN: ROADMAP.md D2: AutoJs6 6.7.0 内置的版本, 顺序同启动页.
        val D2_VERSIONS = linkedMapOf(
            "org.apache.commons:commons-lang3" to "3.18.0",
            "commons-io:commons-io" to "2.8.0",
            "com.fasterxml.jackson.core:jackson-databind" to "2.13.4.2",
            "com.fasterxml.jackson.core:jackson-core" to "2.13.4",
            "com.fasterxml.jackson.core:jackson-annotations" to "2.13.4",
        )

        val VERSION = Regex("""^([\w-]+) = "([^"]+)"$""")
        val LIBRARY = Regex("""module = "([^"]+)", version\.ref = "([^"]+)"""")
        val KEEP = Regex("""^-keep class ([\w.]+)\.\*\* \{ \*; \}$""", RegexOption.MULTILINE)
        val NOTICES_ROW = Regex("""^\| [^|]+ \| `([^`]+)` \| ([^ |]+) \|""", RegexOption.MULTILINE)
        val NOTICE_HEADING = Regex("""^### (.+?) \(`META-INF/NOTICE""", RegexOption.MULTILINE)
        val NOTICE_BLOCK = Regex("""^```text\R(.*?)\R```$""", setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))
        val CLASS_NAME = Regex("""\b[a-z]+(?:\.[a-z0-9]+)+\.[A-Z]\w*""")

    }

}
