<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>Apache Commons Lang3, Commons IO and Jackson for direct use as Java classes in AutoJs6 scripts</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Java-Libraries?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Languages

******

The README.md file is currently available in the following languages:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-TW.md)
- English [en] # current
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ar.md)

******

### Introduction

******

Java Libraries brings back the three Java libraries that used to be built into AutoJs6: Apache Commons Lang3, Apache Commons IO and Jackson. Starting with AutoJs6 6.8.0, the AutoJs6 package no longer includes them; with this plugin installed and enabled in AutoJs6 6.8.1 or later, scripts use classes such as `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils` and `com.fasterxml.jackson.databind.ObjectMapper` by their full names, exactly as with AutoJs6 6.7.0 and earlier.

The plugin is only a resource package: AutoJs6 loads the classes straight from the plugin package, scripts never start the plugin process, and the plugin asks for no permission. Scripts that do not refer to these classes are not affected, and classes nobody uses are never loaded; they only take up space in the package.

******

### Included libraries

******

The classes of each library are in the Java package of the table below, in the versions AutoJs6 6.7.0 included. The licenses and NOTICE files of the libraries are in [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md), and the plugin page shows them too.

| Library | Maven artifacts and versions | Java package | License |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### Usage

******

1. Use AutoJs6 6.8.1 (build 6640) or later.
2. Install Java Libraries from the Plugin Center of AutoJs6 (or download the APK from [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases)) and make sure the plugin is enabled.
3. Use the classes of the libraries by their full names in scripts (or with `importClass` or `importPackage` first), for example:

```javascript
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
```

> There is a single universal package without native libraries, for devices with Android 7.0 (API 24) or later.

******

### Packaged apps

******

When AutoJs6 packages scripts into a standalone app, it can take the classes of this plugin along, so the packaged app can use the libraries on devices without the plugin:

- While packaging, AutoJs6 scans the scripts of the project and takes the classes along as soon as it finds a name that starts with `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` (encrypted scripts and files over 4 MB are not scanned).
- When a name is put together from strings and the scan cannot find it, tick Java Libraries in the list of libraries on the packaging page.
- Taking the classes along needs the plugin installed and enabled on the device that packages the app; when it cannot be done, packaging still completes and the completion dialog lists the plugins left out and what to do.
- The packaged app uses the copy it carries first, and the plugin installed on the device when it carries none.

******

### When the plugin is unavailable

******

When the plugin is not installed, not authorized, disabled in the Plugin Center or too old, scripts cannot use these classes:

- Naming a class is not an error by itself (Rhino treats a name it cannot find as a Java package); the error comes when a script calls a method, constructs an object or passes the name to `importClass`.
- When a script ends with such an error, AutoJs6 prints guidance after the error in the console, saying whether to install, authorize, enable or update the plugin; errors the script catches itself get no guidance.
- When a packaged app carries no copy and its device has no usable plugin either, the guidance says to install and enable the plugin on the device.

******

### Compatibility

******

- Requires AutoJs6 6.8.1 (build 6640) or later. AutoJs6 6.7.0 and earlier include these libraries themselves and do not need the plugin; AutoJs6 6.8.0 cannot use the plugin.
- Supports Android 7.0 (API 24) or later, one package for all processor architectures.
- The versions of the libraries are the ones AutoJs6 6.7.0 included, so existing scripts need no change.
- As in AutoJs6 6.7.0, the libraries go through core library desugaring: methods with `java.time` parameters or results use the desugared `j$.time` types.
- `java.lang.Class.forName` cannot find the classes of the plugin by name; refer to them by their full names or use `importClass`.

******

### Package size

******

The release package is about 1.2 MB. The jars of the three libraries add up to about 2.9 MB; the package keeps every class under its original name, and dex with its shared constant pool and compression make it smaller.

******

### FAQ

******

#### Why does AutoJs6 no longer include these libraries?

Only some scripts use them. As a plugin they keep the AutoJs6 package smaller, and whoever needs them installs the plugin. The libraries in the plugin are the versions AutoJs6 6.7.0 included.

#### Why not upgrade to newer versions of the libraries?

Newer versions no longer support Android 7.x: Jackson requires Android 8.0 from 2.14 on, and newer Commons IO versions fail on Android 7.x. To work on every device AutoJs6 supports and stay the same for existing scripts, the plugin keeps the versions of AutoJs6 6.7.0.

#### Does the plugin go online or run in the background?

No. The plugin asks for no permission, and running a script never starts the plugin process; the classes run in AutoJs6, which runs the script.

#### The error says the plugin needs to be authorized. What should I do?

The release from the Plugin Center of AutoJs6 or from the Releases of this project needs no authorization. A build of your own has a different signature and must first be authorized in the Plugin Center as prompted.

#### Will other libraries AutoJs6 no longer includes (such as ICU4J) join this plugin?

Not for now. ICU4J is very large and most of its uses can switch to `android.icu` of the system, and Log4j 1.x is no longer maintained. When a script uses these libraries, AutoJs6 explains that they are no longer included.

******

### Plugin interface

******

The following information targets AutoJs6 host and plugin developers; the host uses these identifiers to discover the plugin and load its classes:

```text
application id: io.github.supermonster003.autojs6.plugin.java.libraries
plugin id: java-libraries
engine: java-libraries
info service: org.autojs.plugin.INFO (category java-libraries)
exported packages: meta-data org.autojs.plugin.javaPackages.EXPORTS = org.apache.commons.lang3,org.apache.commons.io,com.fasterxml.jackson
wake action: org.autojs.plugin.action.WAKE
minimum host build: 6640
native library: none (all ABIs)
```

******

### Roadmap

******

The pre-release checklist, the decisions and the release steps are tracked in ROADMAP.md; the host loads the classes with the package `org.autojs.autojs.core.plugin.javapackage` of the AutoJs6 repository.

- [View ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### Release history

******

#### v1.0.0

_2026/11/05_

- `Hint` Requires AutoJs6 6.8.1 (build 6640) or later
- `Hint` Starting with AutoJs6 6.8.0, Apache Commons Lang3, Commons IO and Jackson are no longer built in; with this plugin installed and enabled, scripts use the classes of these libraries by their full names as with AutoJs6 6.7.0, and existing scripts need no change
- `Feature` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 and Jackson 2.13.4.2 (core and annotations 2.13.4), the versions AutoJs6 6.7.0 included
- `Feature` The plugin is only a resource package: AutoJs6 loads the classes straight from the plugin, running scripts never starts the plugin process, and the plugin asks for no permission
- `Feature` Apps packaged with AutoJs6 can carry these classes (found by scanning the scripts, or ticked in the list of libraries) and then use them on devices without the plugin
- `Feature` The plugin page shows the version, license and Java package of each library, an example, the NOTICE files and the release history
- `Feature` The plugin page follows the language, night mode and theme color of AutoJs6, and the system when AutoJs6 is not installed or its settings cannot be read
- `Dependency` Attach AutoJs6 Common Plugin API
- `Dependency` Attach desugar_jdk_libs_nio 2.1.5

##### For more release history, refer to

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-en.md)

******

### Build

******

This section targets developers who want to build the plugin from source.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### License

******

The plugin code is licensed under the [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE). The three libraries use the Apache License 2.0 as well, with their NOTICE files in [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md); the package also contains the core library desugaring runtime of Android (GNU GPL v2 with the Classpath Exception).

******

### Related links

******

- AutoJs6 project: https://github.com/SuperMonster003/AutoJs6
- Third-party notices: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
