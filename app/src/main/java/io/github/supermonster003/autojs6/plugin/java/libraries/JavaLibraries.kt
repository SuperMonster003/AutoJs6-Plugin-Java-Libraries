package io.github.supermonster003.autojs6.plugin.java.libraries

/**
 * A library of this plugin (ROADMAP.md D1, D2): its Maven artifacts with their versions, its license, the Java package
 * AutoJs6 scripts reach it by and the NOTICE file it ships (ROADMAP.md D18, as in THIRD_PARTY_NOTICES.md).
 * zh-CN: 本插件收录的一个库 (ROADMAP.md D1, D2): Maven 构件及其版本, 许可证, AutoJs6 脚本访问它所用的 Java 包, 以及它随附的
 * NOTICE 文件 (ROADMAP.md D18, 同 THIRD_PARTY_NOTICES.md).
 */
internal data class Library(
    val name: String,
    val artifacts: List<Artifact>,
    val license: String,
    val exportedPackage: String,
    val url: String,
    val notice: String,
) {
    data class Artifact(val module: String, val version: String)
}

/**
 * The libraries of the launch page, in the order of [JavaLibrariesContract.EXPORTED_PACKAGES]. The versions come from
 * gradle/libs.versions.toml through BuildConfig, so the page always shows what the APK contains.
 * zh-CN: 启动页列出的库, 顺序同 [JavaLibrariesContract.EXPORTED_PACKAGES]. 版本经 BuildConfig 取自 gradle/libs.versions.toml,
 * 因此页面显示的总是 APK 实际包含的版本.
 */
internal object JavaLibraries {

    const val LICENSE = "Apache-2.0"

    // The NOTICE files as the libraries publish them (META-INF of each jar; jackson-databind and jackson-core ship the
    // same text, jackson-annotations none). JavaLibrariesCatalogTest compares them with THIRD_PARTY_NOTICES.md.
    // zh-CN: 各库发布的 NOTICE 文件原文 (各 jar 的 META-INF; jackson-databind 与 jackson-core 内容相同, jackson-annotations 没有).
    // JavaLibrariesCatalogTest 将其与 THIRD_PARTY_NOTICES.md 比对.

    private val NOTICE_COMMONS_LANG = """
        Apache Commons Lang
        Copyright 2001-2025 The Apache Software Foundation

        This product includes software developed at
        The Apache Software Foundation (https://www.apache.org/).
    """.trimIndent()

    private val NOTICE_COMMONS_IO = """
        Apache Commons IO
        Copyright 2002-2020 The Apache Software Foundation

        This product includes software developed at
        The Apache Software Foundation (https://www.apache.org/).
    """.trimIndent()

    private val NOTICE_JACKSON = """
        # Jackson JSON processor

        Jackson is a high-performance, Free/Open Source JSON processing library.
        It was originally written by Tatu Saloranta (tatu.saloranta@iki.fi), and has
        been in development since 2007.
        It is currently developed by a community of developers.

        ## Licensing

        Jackson 2.x core and extension components are licensed under Apache License 2.0
        To find the details that apply to this artifact see the accompanying LICENSE file.

        ## Credits

        A list of contributors may be found from CREDITS(-2.x) file, which is included
        in some artifacts (usually source distributions); but is always available
        from the source code management (SCM) system project uses.
    """.trimIndent()

    val ALL = listOf(
        Library(
            name = "Apache Commons Lang",
            artifacts = listOf(Library.Artifact("org.apache.commons:commons-lang3", BuildConfig.COMMONS_LANG3_VERSION)),
            license = LICENSE,
            exportedPackage = "org.apache.commons.lang3",
            url = "https://commons.apache.org/proper/commons-lang/",
            notice = NOTICE_COMMONS_LANG,
        ),
        Library(
            name = "Apache Commons IO",
            artifacts = listOf(Library.Artifact("commons-io:commons-io", BuildConfig.COMMONS_IO_VERSION)),
            license = LICENSE,
            exportedPackage = "org.apache.commons.io",
            url = "https://commons.apache.org/proper/commons-io/",
            notice = NOTICE_COMMONS_IO,
        ),
        Library(
            name = "Jackson",
            artifacts = listOf(
                Library.Artifact("com.fasterxml.jackson.core:jackson-databind", BuildConfig.JACKSON_DATABIND_VERSION),
                Library.Artifact("com.fasterxml.jackson.core:jackson-core", BuildConfig.JACKSON_CORE_VERSION),
                Library.Artifact("com.fasterxml.jackson.core:jackson-annotations", BuildConfig.JACKSON_ANNOTATIONS_VERSION),
            ),
            license = LICENSE,
            exportedPackage = "com.fasterxml.jackson",
            url = "https://github.com/FasterXML/jackson",
            notice = NOTICE_JACKSON,
        ),
    )

    /**
     * The script of the launch page section "Use in scripts": the classes of appendix C, reached by their full names as
     * in AutoJs6 6.7.0 and earlier.
     * zh-CN: 启动页 "在脚本中使用" 一节的脚本: 以全名访问附录 C 的类, 写法与 AutoJs6 6.7.0 及更早版本相同.
     */
    val EXAMPLE = """
        let StringUtils = org.apache.commons.lang3.StringUtils;
        console.log(StringUtils.isBlank("  ")); // true
        console.log(StringUtils.capitalize("autojs6")); // Autojs6

        let IOUtils = org.apache.commons.io.IOUtils;
        let input = new java.io.ByteArrayInputStream(new java.lang.String("hello").getBytes("UTF-8"));
        console.log(IOUtils.toString(input, "UTF-8")); // hello

        let mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        let tree = mapper.readTree('{"name": "AutoJs6", "version": 6}');
        console.log(tree.get("version").asInt()); // 6
        console.log(mapper.writeValueAsString(tree)); // {"name":"AutoJs6","version":6}
    """.trimIndent()

}
