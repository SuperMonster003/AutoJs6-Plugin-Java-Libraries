import org.gradle.api.GradleException
import org.gradle.api.artifacts.result.ResolvedComponentResult
import org.gradle.api.provider.Property
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Properties
import java.util.zip.ZipFile

plugins {
    id("io.github.supermonster003.autojs6-native-alignment")
    id("org.autojs.build.utils")
    id("org.autojs.build.versions")
    id("org.autojs.build.signs")
    id("org.autojs.build.jvm-convention")
    id("com.android.application")
}

val globalApplicationId = "io.github.supermonster003.autojs6.plugin.java.libraries"

// The AutoJs6 build that loads exported Java packages from official plugins (ROADMAP.md D17, appendix D Q7). The released
// AutoJs6 6.8.0 is build 6640 and has no Java package exports, so the minimum is 6641: 6.8.0 lists the plugin as needing
// a newer host, and 6.8.1 is released with a larger build number. .python/check_repository.py keeps this value equal to
// .readme/common.json.
// zh-CN: 能从官方插件加载导出 Java 包的 AutoJs6 构建 (ROADMAP.md D17, 附录 D Q7). 已发布的 AutoJs6 6.8.0 构建号为 6640,
// 没有 Java 包导出, 所以最低为 6641: 6.8.0 把本插件列为需要更高版本, 6.8.1 发布时构建号更大. .python/check_repository.py
// 保证此值与 .readme/common.json 一致.
val requiredHostVersionCode = 6641L

/**
 * The libraries AutoJs6 loads from this APK (ROADMAP.md D2), with the versions of gradle/libs.versions.toml. They are
 * declared with strictly(), so a transitive dependency that asks for another version fails the resolution instead of
 * raising it; [verifyExportedLibraryVersions] checks the resolved runtime classpath as well.
 * zh-CN: AutoJs6 从本 APK 加载的库 (ROADMAP.md D2), 版本取自 gradle/libs.versions.toml. 以 strictly() 声明, 传递依赖要求其他版本时
 * 解析失败而不是被升级; [verifyExportedLibraryVersions] 另外核对解析后的运行时类路径.
 */
val exportedLibraryVersions = mapOf(
    "org.apache.commons:commons-lang3" to libs.versions.commons.lang3.get(),
    "commons-io:commons-io" to libs.versions.commons.io.get(),
    "com.fasterxml.jackson.core:jackson-databind" to libs.versions.jackson.databind.get(),
    "com.fasterxml.jackson.core:jackson-core" to libs.versions.jackson.core.get(),
    "com.fasterxml.jackson.core:jackson-annotations" to libs.versions.jackson.annotations.get(),
)

/**
 * ROADMAP.md appendix C: classes that must keep their original names in the release dex (D10), one or more per exported
 * package. [verifyReleaseDexClasses] checks them in the release APK.
 * zh-CN: ROADMAP.md 附录 C: 必须以原名存在于 release dex 中的类 (D10), 每个导出包至少一个. 由 [verifyReleaseDexClasses] 在 release APK 中检查.
 */
val representativeClasses = listOf(
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

var isSignsValid = false

android {
    namespace = globalApplicationId
    compileSdk = versions.sdkVersionCompile

    defaultConfig {
        applicationId = globalApplicationId
        minSdk = versions.sdkVersionMin
        targetSdk = versions.sdkVersionTarget
        versionCode = versions.appVersionCode
        versionName = versions.appVersionName

        buildConfigField("String", "VERSION_DATE", "\"${utils.getDateString("MMM d, yyyy", "GMT+08:00")}\"")
        buildConfigField("long", "REQUIRED_HOST_VERSION_CODE", "${requiredHostVersionCode}L")
        buildConfigField("String", "COMMONS_LANG3_VERSION", "\"${libs.versions.commons.lang3.get()}\"")
        buildConfigField("String", "COMMONS_IO_VERSION", "\"${libs.versions.commons.io.get()}\"")
        buildConfigField("String", "JACKSON_DATABIND_VERSION", "\"${libs.versions.jackson.databind.get()}\"")
        buildConfigField("String", "JACKSON_CORE_VERSION", "\"${libs.versions.jackson.core.get()}\"")
        buildConfigField("String", "JACKSON_ANNOTATIONS_VERSION", "\"${libs.versions.jackson.annotations.get()}\"")

        manifestPlaceholders["requiredHostVersion"] = requiredHostVersionCode

        // Routing metadata read by the official plugin index generator.
        // zh-CN: 官方插件索引生成器读取的路由元数据.
        resValue("string", "plugin_id", "java-libraries")
        resValue("string", "plugin_engine", "java-libraries")
        resValue("string", "plugin_variant", "default")
        resValue("string", "plugin_requires_host_version", "$requiredHostVersionCode")
    }

    signingConfigs {
        val props = Properties().also { properties ->
            File("${project.rootDir}/sign.properties").takeIf(File::exists)?.let { signFile ->
                signFile.inputStream().use(properties::load)
                isSignsValid = properties.isNotEmpty()
            }
        }
        if (isSignsValid) {
            create("release") {
                val configuredStoreFile = props.getProperty("storeFile")
                storeFile = configuredStoreFile?.let { configured ->
                    val path = File(configured)
                    when {
                        path.isAbsolute -> path
                        rootProject.file(configured).isFile -> rootProject.file(configured)
                        else -> project.file(configured)
                    }
                }
                keyPassword = props.getProperty("keyPassword")
                keyAlias = props.getProperty("keyAlias")
                storePassword = props.getProperty("storePassword")
            }
        }
    }

    buildTypes {
        val releaseSigningConfig = takeIf { isSignsValid }?.let { signingConfigs.getByName("release") }
        debug {
            isMinifyEnabled = false
            releaseSigningConfig?.let { signingConfig = it }
        }
        release {
            // The plugin's own code is shrunk as usual; proguard-rules.pro keeps the exported packages whole (D10).
            // zh-CN: 插件自身代码照常缩减; proguard-rules.pro 完整保留导出包 (D10).
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            releaseSigningConfig?.let { signingConfig = it }
        }
    }

    compileOptions {
        // The libraries use java.time and java.nio.file, which API 24 / 25 lack; desugared as in AutoJs6 6.7.0 (D11).
        // zh-CN: 这些库使用 API 24 / 25 没有的 java.time 与 java.nio.file; 与 AutoJs6 6.7.0 一样脱糖 (D11).
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        buildConfig = true
        resValues = true
    }

    packaging {
        resources {
            // The license and notice files of the libraries are in THIRD_PARTY_NOTICES.md and on the launch page (D18);
            // the Maven metadata and the Java 9 module descriptors are of no use on Android.
            // zh-CN: 各库的许可证与 NOTICE 文件已写进 THIRD_PARTY_NOTICES.md 与启动页 (D18); Maven 元数据与 Java 9 模块描述在 Android 上无用.
            excludes += listOf(
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/maven/**",
                "META-INF/versions/9/module-info.class",
                "module-info.class",
            )
        }
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            val outputFileNameProperty = output.javaClass.methods.firstOrNull {
                it.name == "getOutputFileName" && it.parameterTypes.isEmpty()
            }?.invoke(output) as? Property<*>
            @Suppress("UNCHECKED_CAST")
            (outputFileNameProperty as? Property<String>)?.set(
                output.versionName.map { versionName ->
                    "${rootProject.name}-v$versionName-universal.apk".lowercase()
                },
            )
        }
    }
}

dependencies {
    implementation(files("$rootDir/libs/common-plugin-api.aar"))

    // ROADMAP.md D2: exactly these versions; strictly() refuses an upgrade by a transitive dependency.
    // zh-CN: ROADMAP.md D2: 版本锁死; strictly() 拒绝传递依赖带来的升级.
    implementation(libs.commons.lang3) { version { strictly(libs.versions.commons.lang3.get()) } }
    implementation(libs.commons.io) { version { strictly(libs.versions.commons.io.get()) } }
    implementation(libs.jackson.databind) { version { strictly(libs.versions.jackson.databind.get()) } }
    implementation(libs.jackson.core) { version { strictly(libs.versions.jackson.core.get()) } }
    implementation(libs.jackson.annotations) { version { strictly(libs.versions.jackson.annotations.get()) } }

    coreLibraryDesugaring(libs.desugar)

    testImplementation(libs.junit)
}

/**
 * Checks that the runtime classpaths resolve every exported library to the version of gradle/libs.versions.toml (D2).
 * zh-CN: 检查运行时类路径把每个导出库解析为 gradle/libs.versions.toml 中的版本 (D2).
 */
val verifyExportedLibraryVersions = tasks.register("verifyExportedLibraryVersions") {
    group = "verification"
    description = "Verifies the resolved versions of the exported Java libraries"
    doLast {
        listOf("debugRuntimeClasspath", "releaseRuntimeClasspath").forEach { name ->
            val resolved = configurations.getByName(name).incoming.resolutionResult.allComponents
                .mapNotNull { component: ResolvedComponentResult -> component.moduleVersion }
                .associate { "${it.group}:${it.name}" to it.version }
            exportedLibraryVersions.forEach { (module, version) ->
                val actual = resolved[module] ?: throw GradleException("$name does not contain $module")
                if (actual != version) throw GradleException("$name resolves $module to $actual instead of $version")
            }
            logger.lifecycle("$name: ${exportedLibraryVersions.entries.joinToString { "${it.key.substringAfter(':')} ${it.value}" }}")
        }
    }
}

/**
 * The classes a dex file defines (its class_defs), with Java names. The dex format is little endian; a class_def_item is
 * 32 bytes long and starts with the type index, a type_id_item holds the string index of the descriptor, and a string
 * is a ULEB128 length followed by MUTF-8 bytes ending with 0 (plain ASCII for these class names).
 * zh-CN: dex 文件定义的类 (class_defs), 使用 Java 名称. dex 为小端序; class_def_item 长 32 字节, 以类型索引开头; type_id_item 存放
 * 描述符的字符串索引; 字符串由 ULEB128 长度与以 0 结尾的 MUTF-8 字节组成 (这些类名都是 ASCII).
 */
fun dexClassNames(bytes: ByteArray): Set<String> {
    if (bytes.size < 0x70 || String(bytes, 0, 3, Charsets.US_ASCII) != "dex") throw GradleException("Not a dex file")
    val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
    val stringIdsOffset = buffer.getInt(0x3C)
    val typeIdsOffset = buffer.getInt(0x44)
    val classDefsSize = buffer.getInt(0x60)
    val classDefsOffset = buffer.getInt(0x64)
    fun string(index: Int): String {
        var offset = buffer.getInt(stringIdsOffset + index * 4)
        while (bytes[offset++].toInt() and 0x80 != 0) Unit
        var end = offset
        while (bytes[end] != 0.toByte()) end++
        return String(bytes, offset, end - offset, Charsets.UTF_8)
    }
    return (0 until classDefsSize).mapTo(HashSet()) { index ->
        val typeIndex = buffer.getInt(classDefsOffset + index * 32)
        string(buffer.getInt(typeIdsOffset + typeIndex * 4)).removePrefix("L").removeSuffix(";").replace('/', '.')
    }
}

/**
 * The release check of D10 (ROADMAP.md P0.1): the release APK has no native library, and its dex files define every class
 * of appendix C under its original name. Prints the APK and dex sizes. Runs on the unsigned APK of CI as well; the signed
 * release gate depends on it.
 * zh-CN: D10 的发布校验 (ROADMAP.md P0.1): release APK 不含原生库, 其 dex 以原名定义附录 C 的每个类. 输出 APK 与 dex 体积.
 * 也能在 CI 的未签名 APK 上运行; 签名发布门禁依赖它.
 */
val verifyReleaseDexClasses = tasks.register("verifyReleaseDexClasses") {
    group = "verification"
    description = "Verifies that the release dex keeps the representative classes of the exported packages"
    dependsOn("assembleRelease")
    doLast {
        val directory = layout.buildDirectory.dir("outputs/apk/release").get().asFile
        val apk = directory.listFiles { file -> file.isFile && file.extension == utils.FILE_EXTENSION_APK }.orEmpty().singleOrNull()
            ?: throw GradleException("Expected one release APK in $directory")
        ZipFile(apk).use { zip ->
            val entries = zip.entries().asSequence().toList()
            entries.firstOrNull { it.name.startsWith("lib/") || it.name.endsWith(".so") }?.let {
                throw GradleException("The release APK must not contain native libraries: ${it.name}")
            }
            val dexEntries = entries.filter { Regex("classes\\d*\\.dex").matches(it.name) }.sortedBy { it.name }
            val defined = dexEntries.flatMapTo(HashSet()) { entry -> dexClassNames(zip.getInputStream(entry).use { it.readBytes() }) }
            val missing = representativeClasses.filterNot(defined::contains)
            if (missing.isNotEmpty()) throw GradleException("The release dex misses (or renamed) $missing")
            logger.lifecycle("Release APK: ${apk.name}, ${apk.length()} bytes")
            dexEntries.forEach { logger.lifecycle("  ${it.name}: ${it.size} bytes (${it.compressedSize} compressed)") }
            logger.lifecycle("  ${defined.size} classes; ${representativeClasses.size} representative classes present")
        }
    }
}

tasks {
    withType(JavaCompile::class.java) { options.encoding = "UTF-8" }
    named("preBuild") { dependsOn(verifyExportedLibraryVersions) }

    // Collects the signed release APK into releases/ with its CRC32 appended (the official plugin convention).
    // zh-CN: 把签名的 release APK 收集到 releases/ 并在文件名后追加 CRC32 (官方插件惯例).
    register<Copy>("appendDigestToReleasedFiles") {
        group = "distribution"
        description = "Appends CRC32 digest to released APK files"
        dependsOn("assembleRelease")
        from(layout.buildDirectory.dir("outputs/apk/release")) {
            include("*.${utils.FILE_EXTENSION_APK}")
            eachFile { name = "${name.removeSuffix(".${utils.FILE_EXTENSION_APK}")}-${utils.digestCRC32(file)}.${utils.FILE_EXTENSION_APK}" }
        }
        into(rootProject.layout.projectDirectory.dir("releases"))
        includeEmptyDirs = false
        duplicatesStrategy = DuplicatesStrategy.FAIL
        doLast { println("Destination: ${rootProject.layout.projectDirectory.dir("releases").asFile}") }
    }
}

// Fail before collection when the signing identity or the APK is not what AutoJs6 expects: complete credentials and one
// universal APK that apksigner accepts; verifyReleaseDexClasses checks the content.
// zh-CN: 签名身份或 APK 不符合 AutoJs6 的预期时, 在收集之前失败: 凭据完整, 只有一个通过 apksigner 校验的通用 APK; 内容由
// verifyReleaseDexClasses 检查.
val verifySignedReleaseArtifacts = tasks.register("verifySignedReleaseArtifacts") {
    group = "verification"
    dependsOn("assembleRelease", verifyReleaseDexClasses)
    doLast {
        val signing = android.buildTypes.getByName("release").signingConfig
        check(
            signing != null && signing.storeFile?.isFile == true &&
                !signing.storePassword.isNullOrBlank() && !signing.keyAlias.isNullOrBlank() &&
                !signing.keyPassword.isNullOrBlank(),
        ) { "Release signing configuration is missing or incomplete" }
        val directory = layout.buildDirectory.dir("outputs/apk/release").get().asFile
        val apks = directory.listFiles { file -> file.isFile && file.extension == utils.FILE_EXTENSION_APK }.orEmpty()
        check(apks.map { it.name }.toSet() == setOf("${rootProject.name}-v${versions.appVersionName}-universal.apk")) {
            "Unexpected release APK set: ${apks.map { it.name }.sorted()}"
        }
        val signerJar = androidComponents.sdkComponents.sdkDirectory.get().asFile
            .resolve("build-tools/${android.buildToolsVersion}/lib/apksigner.jar")
        check(signerJar.isFile) { "Android SDK apksigner is unavailable" }
        val result = providers.exec {
            commandLine("java", "-jar", signerJar.absolutePath, "verify", apks.single().absolutePath)
            isIgnoreExitValue = true
        }.result.get()
        check(result.exitValue == 0) { "Release APK signature verification failed" }
    }
}
tasks.named("appendDigestToReleasedFiles") { dependsOn(verifySignedReleaseArtifacts) }

// The plugin ships Java classes only.
// zh-CN: 插件只包含 Java 类.
nativeAlignment { expectNoNativeLibraries.set(true) }
