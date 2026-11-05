<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>供 AutoJs6 腳本以 Java 類直接調用的 Apache Commons Lang3, Commons IO 與 Jackson</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Java-Libraries?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 語言 (Languages)

******

目前 README.md 支援以下語言:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hans.md)
- 繁體中文 (香港) [zh-Hant-HK] # 目前
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ar.md)

******

### 簡介

******

Java Libraries 把 AutoJs6 過去內置的三個 Java 庫裝回裝置: Apache Commons Lang3, Apache Commons IO 與 Jackson. 自 AutoJs6 6.8.0 起, 這些庫不再隨 AutoJs6 安裝包提供; 在 AutoJs6 6.8.1 或更高版本中安裝並啟用本插件後, 腳本照常以全名使用 `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` 等類, 寫法與 AutoJs6 6.7.0 及更早版本相同.

插件本身只是一個資源包: AutoJs6 直接從插件的安裝包載入這些類, 腳本運行時不會啟動插件進程, 插件也不申請任何權限. 不引用這些類的腳本不受影響, 用不到的類不會被載入, 只佔用安裝包體積.

******

### 收錄的庫

******

各庫的類位於下表的 Java 包中, 版本與 AutoJs6 6.7.0 內置的相同. 各庫的授權與 NOTICE 文件詳見 [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md), 插件頁面也會顯示.

| 庫 | Maven 構件與版本 | Java 包 | 授權 |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### 使用方法

******

1. 使用 AutoJs6 6.8.1 (構建 6640) 或更高版本.
2. 在 AutoJs6 的插件中心安裝 Java Libraries (也可以從 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases) 下載 APK 安裝), 並確認插件處於啟用狀態.
3. 在腳本中以全名使用庫中的類 (也可以先 `importClass` 或 `importPackage`), 例如:

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

> 插件只有一個通用安裝包, 不含原生庫, 支援 Android 7.0 (API 24) 及以上的裝置.

******

### 打包應用

******

用 AutoJs6 把腳本打包成獨立應用時, 可以把本插件的類一併帶入, 打包後的應用在沒有安裝本插件的裝置上也能使用這些庫:

- 打包時 AutoJs6 掃描項目中的腳本, 發現以 `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` 開頭的名字即自動帶入 (加密的腳本與 4 MB 以上的文件不掃描).
- 名字由字串拼接而成, 掃描不到時, 可以在打包頁的庫列表中勾選 Java Libraries.
- 帶入要求打包所用的裝置上已安裝並啟用本插件; 無法帶入時打包照常完成, 完成對話框會列出未帶入的插件與做法.
- 打包應用優先使用帶入的副本, 沒有帶入時使用裝置上已安裝的本插件.

******

### 插件不可用時

******

插件未安裝, 未授權, 在插件中心被停用, 或版本過舊時, 腳本無法使用這些類:

- 引用類名本身不會立即出錯 (Rhino 把找不到的名字當作 Java 包), 調用方法, 構造對象或 `importClass` 時才出錯.
- 腳本因這類錯誤結束時, AutoJs6 在控制台的錯誤信息之後給出指引, 說明需要安裝, 授權, 啟用還是更新插件; 腳本自己 `catch` 的錯誤不附指引.
- 打包應用既沒有帶入的副本, 所在裝置上也沒有可用的本插件時, 指引會說明需要在裝置上安裝並啟用本插件.

******

### 相容性

******

- 需要 AutoJs6 6.8.1 (構建 6640) 或更高版本. AutoJs6 6.7.0 及更早版本自帶這些庫, 不需要本插件; AutoJs6 6.8.0 不能使用本插件.
- 支援 Android 7.0 (API 24) 及以上, 一個安裝包適用於所有處理器架構.
- 庫的版本與 AutoJs6 6.7.0 內置的相同, 原有腳本無需修改.
- 與 AutoJs6 6.7.0 相同, 這些庫經過 core library desugaring: 參數或返回值為 `java.time` 類型的方法使用的是脫糖後的 `j$.time` 類型.
- `java.lang.Class.forName` 按名字查找時找不到本插件的類, 請以全名引用或使用 `importClass`.

******

### 安裝包體積

******

release 安裝包約 1.2 MB. 三個庫的 jar 合計約 2.9 MB; 安裝包中的類以原名完整保留, 經 dex 合併常量池與壓縮後體積更小.

******

### 常見問題

******

#### 為甚麼 AutoJs6 不再內置這些庫?

只有一部分腳本用到這些庫. 移到插件後 AutoJs6 安裝包更小, 需要的人安裝插件即可. 插件中的庫與 AutoJs6 6.7.0 內置的版本相同.

#### 為甚麼不升級到這些庫的新版本?

新版本不再支援 Android 7.x: Jackson 2.14 起要求 Android 8.0, 更新的 Commons IO 在 Android 7.x 上會出錯. 為了讓 AutoJs6 支援的所有裝置都能使用, 並與原有腳本保持一致, 版本沿用 AutoJs6 6.7.0.

#### 插件會連網或在背景運行嗎?

不會. 插件不申請任何權限, 腳本運行時也不會啟動插件進程; 這些類在運行腳本的 AutoJs6 中執行.

#### 錯誤提示需要授權插件怎麼辦?

從 AutoJs6 插件中心或本項目 Releases 安裝的正式版無需授權. 自行構建的版本簽名不同, 需要先在插件中心按提示授權.

#### AutoJs6 不再內置的其他庫 (如 ICU4J) 會加入本插件嗎?

目前不會. ICU4J 體積很大且多數用法可以改用系統的 `android.icu`, Log4j 1.x 已停止維護. 腳本用到這些庫時, AutoJs6 會說明它們已不再內置.

******

### 插件接口

******

以下信息面向 AutoJs6 宿主與插件開發者, 宿主通過這些標識發現插件並載入其中的類:

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

### 開發路線圖

******

發布前清單, 決策與發布步驟記錄在 ROADMAP.md 中; 宿主載入這些類的機制位於 AutoJs6 倉庫的 `org.autojs.autojs.core.plugin.javapackage` 包.

- [查看 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### 發行歷史

******

#### v1.0.0

_2026/11/05_

- `提示` 需要 AutoJs6 6.8.1 (構建 6640) 或更高版本
- `提示` AutoJs6 6.8.0 起不再內置 Apache Commons Lang3, Commons IO 與 Jackson; 安裝並啟用本插件後, 腳本照常以全名使用這些庫的類, 寫法與 AutoJs6 6.7.0 相同, 原有腳本無需修改
- `新增` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 與 Jackson 2.13.4.2 (core 與 annotations 為 2.13.4), 版本與 AutoJs6 6.7.0 內置的相同
- `新增` 插件只是一個資源包: AutoJs6 直接從插件載入這些類, 腳本運行時不啟動插件進程, 插件不申請任何權限
- `新增` 用 AutoJs6 打包應用時可以帶入這些類 (自動掃描腳本, 也可以在庫列表中勾選), 打包後的應用在沒有本插件的裝置上也能使用
- `新增` 插件頁面顯示各庫的版本, 授權, Java 包, 使用範例, NOTICE 文件與發行歷史
- `新增` 插件頁面跟隨 AutoJs6 的語言, 夜間模式與主題色, 未安裝 AutoJs6 或無法讀取其設定時跟隨系統
- `依賴` 附加 AutoJs6 Common Plugin API
- `依賴` 附加 desugar_jdk_libs_nio 2.1.5

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-HK.md)

******

### 構建

******

本節面向希望從源碼構建插件的開發者.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### 許可

******

插件代碼使用 [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE). 收錄的三個庫同樣使用 Apache License 2.0, 其 NOTICE 文件詳見 [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md); 安裝包另含 Android 的 core library desugaring 運行庫 (GNU GPL v2 附 Classpath Exception).

******

### 相關連結

******

- AutoJs6 項目: https://github.com/SuperMonster003/AutoJs6
- 第三方聲明: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
