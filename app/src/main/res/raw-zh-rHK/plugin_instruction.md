Java Libraries 把 AutoJs6 過去內置的三個 Java 庫裝回裝置: Apache Commons Lang3, Apache Commons IO 與 Jackson. 自 AutoJs6 6.8.0 起, 這些庫不再隨 AutoJs6 安裝包提供; 在 AutoJs6 6.8.1 或更高版本中安裝並啟用本插件後, 腳本照常以全名使用 `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` 等類, 寫法與 AutoJs6 6.7.0 及更早版本相同.

插件本身只是一個資源包: AutoJs6 直接從插件的安裝包載入這些類, 腳本運行時不會啟動插件進程, 插件也不申請任何權限. 不引用這些類的腳本不受影響, 用不到的類不會被載入, 只佔用安裝包體積.

| 庫 | Maven 構件與版本 | Java 包 | 授權 |
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

### 插件不可用時

插件未安裝, 未授權, 在插件中心被停用, 或版本過舊時, 腳本無法使用這些類:

- 引用類名本身不會立即出錯 (Rhino 把找不到的名字當作 Java 包), 調用方法, 構造對象或 `importClass` 時才出錯.
- 腳本因這類錯誤結束時, AutoJs6 在控制台的錯誤信息之後給出指引, 說明需要安裝, 授權, 啟用還是更新插件; 腳本自己 `catch` 的錯誤不附指引.
- 打包應用既沒有帶入的副本, 所在裝置上也沒有可用的本插件時, 指引會說明需要在裝置上安裝並啟用本插件.

源碼, 發行歷史與第三方授權見 [項目主頁](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
