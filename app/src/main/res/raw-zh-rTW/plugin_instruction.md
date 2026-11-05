Java Libraries 把 AutoJs6 過去內建的三個 Java 函式庫裝回裝置: Apache Commons Lang3, Apache Commons IO 與 Jackson. 自 AutoJs6 6.8.0 起, 這些函式庫不再隨 AutoJs6 安裝套件提供; 在 AutoJs6 6.8.1 或更新版本中安裝並啟用本外掛後, 腳本照常以全名使用 `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` 等類別, 寫法與 AutoJs6 6.7.0 及更早版本相同.

外掛本身只是一個資源套件: AutoJs6 直接從外掛的安裝套件載入這些類別, 腳本執行時不會啟動外掛處理程序, 外掛也不要求任何權限. 不引用這些類別的腳本不受影響, 用不到的類別不會被載入, 只佔用安裝套件的空間.

| 函式庫 | Maven 構件與版本 | Java 套件 | 授權 |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

### 使用方法

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

### 外掛無法使用時

外掛未安裝, 未授權, 在外掛中心被停用, 或版本過舊時, 腳本無法使用這些類別:

- 引用類別名稱本身不會立即出錯 (Rhino 把找不到的名稱當作 Java 套件), 呼叫方法, 建構物件或 `importClass` 時才出錯.
- 腳本因這類錯誤結束時, AutoJs6 在主控台的錯誤訊息之後給出指引, 說明需要安裝, 授權, 啟用還是更新外掛; 腳本自己 `catch` 的錯誤不附指引.
- 打包的應用程式既沒有帶入的副本, 所在裝置上也沒有可用的本外掛時, 指引會說明需要在裝置上安裝並啟用本外掛.

原始碼, 發行歷史與第三方授權見 [專案首頁](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
