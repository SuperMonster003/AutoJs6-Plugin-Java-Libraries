<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>供 AutoJs6 脚本以 Java 类直接调用的 Apache Commons Lang3, Commons IO 与 Jackson</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Java-Libraries?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 语言 (Languages)

******

当前 README.md 支持以下语言:

- 简体中文 [zh-Hans] # 当前
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ar.md)

******

### 简介

******

Java Libraries 把 AutoJs6 过去内置的三个 Java 库装回设备: Apache Commons Lang3, Apache Commons IO 与 Jackson. 自 AutoJs6 6.8.0 起, 这些库不再随 AutoJs6 安装包提供; 在 AutoJs6 6.8.1 或更高版本中安装并启用本插件后, 脚本照常以全名使用 `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` 等类, 写法与 AutoJs6 6.7.0 及更早版本相同.

插件本身只是一个资源包: AutoJs6 直接从插件的安装包加载这些类, 脚本运行时不会启动插件进程, 插件也不申请任何权限. 不引用这些类的脚本不受影响, 用不到的类不会被加载, 只占用安装包体积.

******

### 收录的库

******

各库的类位于下表的 Java 包中, 版本与 AutoJs6 6.7.0 内置的相同. 各库的许可证与 NOTICE 文件详见 [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md), 插件页面也会显示.

| 库 | Maven 构件与版本 | Java 包 | 许可证 |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### 使用方法

******

1. 使用 AutoJs6 6.8.1 (构建 6640) 或更高版本.
2. 在 AutoJs6 的插件中心安装 Java Libraries (也可以从 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases) 下载 APK 安装), 并确认插件处于启用状态.
3. 在脚本中以全名使用库中的类 (也可以先 `importClass` 或 `importPackage`), 例如:

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

> 插件只有一个通用安装包, 不含原生库, 支持 Android 7.0 (API 24) 及以上的设备.

******

### 打包应用

******

用 AutoJs6 把脚本打包成独立应用时, 可以把本插件的类一并带入, 打包后的应用在没有安装本插件的设备上也能使用这些库:

- 打包时 AutoJs6 扫描项目中的脚本, 发现以 `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` 开头的名字即自动带入 (加密的脚本与 4 MB 以上的文件不扫描).
- 名字由字符串拼接而成, 扫描不到时, 可以在打包页的库列表中勾选 Java Libraries.
- 带入要求打包所用的设备上已安装并启用本插件; 无法带入时打包照常完成, 完成对话框会列出未带入的插件与做法.
- 打包应用优先使用带入的副本, 没有带入时使用设备上已安装的本插件.

******

### 插件不可用时

******

插件未安装, 未授权, 在插件中心被停用, 或版本过旧时, 脚本无法使用这些类:

- 引用类名本身不会立即出错 (Rhino 把找不到的名字当作 Java 包), 调用方法, 构造对象或 `importClass` 时才出错.
- 脚本因这类错误结束时, AutoJs6 在控制台的错误信息之后给出指引, 说明需要安装, 授权, 启用还是更新插件; 脚本自己 `catch` 的错误不附指引.
- 打包应用既没有带入的副本, 所在设备上也没有可用的本插件时, 指引会说明需要在设备上安装并启用本插件.

******

### 兼容性

******

- 需要 AutoJs6 6.8.1 (构建 6640) 或更高版本. AutoJs6 6.7.0 及更早版本自带这些库, 不需要本插件; AutoJs6 6.8.0 不能使用本插件.
- 支持 Android 7.0 (API 24) 及以上, 一个安装包适用于所有处理器架构.
- 库的版本与 AutoJs6 6.7.0 内置的相同, 原有脚本无需修改.
- 与 AutoJs6 6.7.0 相同, 这些库经过 core library desugaring: 参数或返回值为 `java.time` 类型的方法使用的是脱糖后的 `j$.time` 类型.
- `java.lang.Class.forName` 按名字查找时找不到本插件的类, 请以全名引用或使用 `importClass`.

******

### 安装包体积

******

release 安装包约 1.0 MB. 三个库的 jar 合计约 2.9 MB; 安装包中的类以原名完整保留, 经 dex 合并常量池与压缩后体积更小.

******

### 常见问题

******

#### 为什么 AutoJs6 不再内置这些库?

只有一部分脚本用到这些库. 移到插件后 AutoJs6 安装包更小, 需要的人安装插件即可. 插件中的库与 AutoJs6 6.7.0 内置的版本相同.

#### 为什么不升级到这些库的新版本?

新版本不再支持 Android 7.x: Jackson 2.14 起要求 Android 8.0, 更新的 Commons IO 在 Android 7.x 上会出错. 为了让 AutoJs6 支持的所有设备都能使用, 并与原有脚本保持一致, 版本沿用 AutoJs6 6.7.0.

#### 插件会联网或在后台运行吗?

不会. 插件不申请任何权限, 脚本运行时也不会启动插件进程; 这些类在运行脚本的 AutoJs6 中执行.

#### 错误提示需要授权插件怎么办?

从 AutoJs6 插件中心或本项目 Releases 安装的正式版无需授权. 自行构建的版本签名不同, 需要先在插件中心按提示授权.

#### AutoJs6 不再内置的其他库 (如 ICU4J) 会加入本插件吗?

目前不会. ICU4J 体积很大且多数用法可以改用系统的 `android.icu`, Log4j 1.x 已停止维护. 脚本用到这些库时, AutoJs6 会说明它们已不再内置.

******

### 插件接口

******

以下信息面向 AutoJs6 宿主与插件开发者, 宿主通过这些标识发现插件并加载其中的类:

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

### 开发路线图

******

发布前清单, 决策与发布步骤记录在 ROADMAP.md 中; 宿主加载这些类的机制位于 AutoJs6 仓库的 `org.autojs.autojs.core.plugin.javapackage` 包.

- [查看 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### 发行历史

******

#### v1.0.0

_2026/11/05_

- `提示` 需要 AutoJs6 6.8.1 (构建 6640) 或更高版本
- `提示` AutoJs6 6.8.0 起不再内置 Apache Commons Lang3, Commons IO 与 Jackson; 安装并启用本插件后, 脚本照常以全名使用这些库的类, 写法与 AutoJs6 6.7.0 相同, 原有脚本无需修改
- `新增` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 与 Jackson 2.13.4.2 (core 与 annotations 为 2.13.4), 版本与 AutoJs6 6.7.0 内置的相同
- `新增` 插件只是一个资源包: AutoJs6 直接从插件加载这些类, 脚本运行时不启动插件进程, 插件不申请任何权限
- `新增` 用 AutoJs6 打包应用时可以带入这些类 (自动扫描脚本, 也可以在库列表中勾选), 打包后的应用在没有本插件的设备上也能使用
- `新增` 插件页面显示各库的版本, 许可证, Java 包, 使用示例, NOTICE 文件与发行历史
- `新增` 插件页面跟随 AutoJs6 的语言, 夜间模式与主题色, 未安装 AutoJs6 或无法读取其设置时跟随系统
- `依赖` 附加 AutoJs6 Common Plugin API
- `依赖` 附加 desugar_jdk_libs_nio 2.1.5

##### 更多发行历史可参阅

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hans.md)

******

### 构建

******

本节面向希望从源码构建插件的开发者.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### 许可

******

插件代码使用 [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE). 收录的三个库同样使用 Apache License 2.0, 其 NOTICE 文件详见 [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md); 安装包另含 Android 的 core library desugaring 运行库 (GNU GPL v2 附 Classpath Exception).

******

### 相关链接

******

- AutoJs6 项目: https://github.com/SuperMonster003/AutoJs6
- 第三方声明: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
