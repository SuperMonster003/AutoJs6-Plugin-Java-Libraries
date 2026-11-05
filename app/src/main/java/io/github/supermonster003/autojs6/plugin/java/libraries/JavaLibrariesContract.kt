package io.github.supermonster003.autojs6.plugin.java.libraries

/**
 * The names AutoJs6 relies on (ROADMAP.md D8, D9): the Plugin Center identity and the exported Java packages.
 * zh-CN: AutoJs6 依赖的名称 (ROADMAP.md D8, D9): 插件中心身份与导出的 Java 包.
 */
internal object JavaLibrariesContract {

    /** Plugin Center id. */
    const val PLUGIN_ID = "java-libraries"

    /** Plugin Center category (the `org.autojs.plugin.INFO` category of the info service). */
    const val ENGINE = "java-libraries"

    /**
     * Resource reference of the localized Plugin Center instruction (`plugin_instruction.md` in the raw folders, generated
     * from `.readme`); AutoJs6 resolves it in the plugin package.
     */
    const val INSTRUCTION_REFERENCE = "@raw/plugin_instruction"

    /**
     * Application meta-data with the comma separated Java packages AutoJs6 loads from this APK (host
     * `PluginJavaPackageCatalog`); AutoJs6 accepts only the packages its catalog assigns to this plugin.
     */
    const val META_DATA_EXPORTS = "org.autojs.plugin.javaPackages.EXPORTS"

    /** The value of [META_DATA_EXPORTS] in AndroidManifest.xml, in the same order. */
    val EXPORTED_PACKAGES = listOf("org.apache.commons.lang3", "org.apache.commons.io", "com.fasterxml.jackson")

}
