<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>供 AutoJs6 腳本以 Java 類別直接呼叫的 Apache Commons Lang3, Commons IO 與 Jackson</p>

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
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-HK.md)
- 繁體中文 (台灣) [zh-Hant-TW] # 目前
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

Java Libraries 把 AutoJs6 過去內建的三個 Java 函式庫裝回裝置: Apache Commons Lang3, Apache Commons IO 與 Jackson. 自 AutoJs6 6.8.0 起, 這些函式庫不再隨 AutoJs6 安裝套件提供; 在 AutoJs6 6.8.1 或更新版本中安裝並啟用本外掛後, 腳本照常以全名使用 `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` 等類別, 寫法與 AutoJs6 6.7.0 及更早版本相同.

外掛本身只是一個資源套件: AutoJs6 直接從外掛的安裝套件載入這些類別, 腳本執行時不會啟動外掛處理程序, 外掛也不要求任何權限. 不引用這些類別的腳本不受影響, 用不到的類別不會被載入, 只佔用安裝套件的空間.

******

### 收錄的函式庫

******

各函式庫的類別位於下表的 Java 套件中, 版本與 AutoJs6 6.7.0 內建的相同. 各函式庫的授權與 NOTICE 檔案詳見 [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md), 外掛頁面也會顯示.

| 函式庫 | Maven 構件與版本 | Java 套件 | 授權 |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### 使用方法

******

1. 使用 AutoJs6 6.8.1 (建置 6641) 或更新版本.
2. 在 AutoJs6 的外掛中心安裝 Java Libraries (也可以從 [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases) 下載 APK 安裝), 並確認外掛處於啟用狀態.
3. 在腳本中以全名使用函式庫中的類別 (也可以先 `importClass` 或 `importPackage`), 例如:

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

> 外掛只有一個通用安裝套件, 不含原生函式庫, 支援 Android 7.0 (API 24) 以上的裝置.

******

### 打包的應用程式

******

用 AutoJs6 把腳本打包成獨立應用程式時, 可以把本外掛的類別一併帶入, 打包後的應用程式在沒有安裝本外掛的裝置上也能使用這些函式庫:

- 打包時 AutoJs6 掃描專案中的腳本, 發現以 `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` 開頭的名稱即自動帶入 (加密的腳本與 4 MB 以上的檔案不掃描).
- 名稱由字串拼接而成, 掃描不到時, 可以在打包頁面的函式庫清單中勾選 Java Libraries.
- 帶入要求打包所用的裝置上已安裝並啟用本外掛; 無法帶入時打包照常完成, 完成對話框會列出未帶入的外掛與做法.
- 打包的應用程式優先使用帶入的副本, 沒有帶入時使用裝置上已安裝的本外掛.

******

### 外掛無法使用時

******

外掛未安裝, 未授權, 在外掛中心被停用, 或版本過舊時, 腳本無法使用這些類別:

- 引用類別名稱本身不會立即出錯 (Rhino 把找不到的名稱當作 Java 套件), 呼叫方法, 建構物件或 `importClass` 時才出錯.
- 腳本因這類錯誤結束時, AutoJs6 在主控台的錯誤訊息之後給出指引, 說明需要安裝, 授權, 啟用還是更新外掛; 腳本自己 `catch` 的錯誤不附指引.
- 打包的應用程式既沒有帶入的副本, 所在裝置上也沒有可用的本外掛時, 指引會說明需要在裝置上安裝並啟用本外掛.

******

### 相容性

******

- 需要 AutoJs6 6.8.1 (建置 6641) 或更新版本. AutoJs6 6.7.0 及更早版本自帶這些函式庫, 不需要本外掛; AutoJs6 6.8.0 不能使用本外掛.
- 支援 Android 7.0 (API 24) 以上, 一個安裝套件適用於所有處理器架構.
- 函式庫的版本與 AutoJs6 6.7.0 內建的相同, 原有腳本無需修改.
- 與 AutoJs6 6.7.0 相同, 這些函式庫經過 core library desugaring: 參數或傳回值為 `java.time` 型別的方法使用的是脫糖後的 `j$.time` 型別.
- `java.lang.Class.forName` 依名稱查找時找不到本外掛的類別, 請以全名引用或使用 `importClass`.

******

### 安裝套件大小

******

release 安裝套件約 1.2 MB. 三個函式庫的 jar 合計約 2.9 MB; 安裝套件中的類別以原名完整保留, 經 dex 合併常數池與壓縮後更小.

******

### 常見問題

******

#### 為什麼 AutoJs6 不再內建這些函式庫?

只有一部分腳本用到這些函式庫. 移到外掛後 AutoJs6 安裝套件更小, 需要的人安裝外掛即可. 外掛中的函式庫與 AutoJs6 6.7.0 內建的版本相同.

#### 為什麼不升級到這些函式庫的新版本?

新版本不再支援 Android 7.x: Jackson 2.14 起要求 Android 8.0, 更新的 Commons IO 在 Android 7.x 上會出錯. 為了讓 AutoJs6 支援的所有裝置都能使用, 並與原有腳本保持一致, 版本沿用 AutoJs6 6.7.0.

#### 外掛會連網或在背景執行嗎?

不會. 外掛不要求任何權限, 腳本執行時也不會啟動外掛處理程序; 這些類別在執行腳本的 AutoJs6 中執行.

#### 錯誤提示需要授權外掛怎麼辦?

從 AutoJs6 外掛中心或本專案 Releases 安裝的正式版無需授權. 自行建置的版本簽章不同, 需要先在外掛中心依提示授權.

#### AutoJs6 不再內建的其他函式庫 (如 ICU4J) 會加入本外掛嗎?

目前不會. ICU4J 檔案很大且多數用法可以改用系統的 `android.icu`, Log4j 1.x 已停止維護. 腳本用到這些函式庫時, AutoJs6 會說明它們已不再內建.

******

### 外掛介面

******

以下資訊面向 AutoJs6 主程式與外掛開發者, 主程式透過這些識別碼發現外掛並載入其中的類別:

```text
application id: io.github.supermonster003.autojs6.plugin.java.libraries
plugin id: java-libraries
engine: java-libraries
info service: org.autojs.plugin.INFO (category java-libraries)
exported packages: meta-data org.autojs.plugin.javaPackages.EXPORTS = org.apache.commons.lang3,org.apache.commons.io,com.fasterxml.jackson
wake action: org.autojs.plugin.action.WAKE
minimum host build: 6641
native library: none (all ABIs)
```

******

### 開發路線圖

******

發布前清單, 決策與發布步驟記錄在 ROADMAP.md 中; 主程式載入這些類別的機制位於 AutoJs6 儲存庫的 `org.autojs.autojs.core.plugin.javapackage` 套件.

- [檢視 ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### 發行歷史

******

#### v1.0.0

_2026/11/05_

- `提示` 需要 AutoJs6 6.8.1 (建置 6641) 或更新版本
- `提示` AutoJs6 6.8.0 起不再內建 Apache Commons Lang3, Commons IO 與 Jackson; 安裝並啟用本外掛後, 腳本照常以全名使用這些函式庫的類別, 寫法與 AutoJs6 6.7.0 相同, 原有腳本無需修改
- `新增` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 與 Jackson 2.13.4.2 (core 與 annotations 為 2.13.4), 版本與 AutoJs6 6.7.0 內建的相同
- `新增` 外掛只是一個資源套件: AutoJs6 直接從外掛載入這些類別, 腳本執行時不啟動外掛處理程序, 外掛不要求任何權限
- `新增` 用 AutoJs6 打包應用程式時可以帶入這些類別 (自動掃描腳本, 也可以在函式庫清單中勾選), 打包後的應用程式在沒有本外掛的裝置上也能使用
- `新增` 外掛頁面顯示各函式庫的版本, 授權, Java 套件, 使用範例, NOTICE 檔案與發行歷史
- `新增` 外掛頁面跟隨 AutoJs6 的語言, 夜間模式與主題色, 未安裝 AutoJs6 或無法讀取其設定時跟隨系統
- `相依性` 附加 AutoJs6 Common Plugin API
- `相依性` 附加 desugar_jdk_libs_nio 2.1.5

##### 更多發行歷史可參閱

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-TW.md)

******

### 建置

******

本節面向希望從原始碼建置外掛的開發者.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### 授權

******

外掛程式碼採用 [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE). 收錄的三個函式庫同樣採用 Apache License 2.0, 其 NOTICE 檔案詳見 [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md); 安裝套件另含 Android 的 core library desugaring 執行階段函式庫 (GNU GPL v2 附 Classpath Exception).

******

### 相關連結

******

- AutoJs6 專案: https://github.com/SuperMonster003/AutoJs6
- 第三方聲明: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
