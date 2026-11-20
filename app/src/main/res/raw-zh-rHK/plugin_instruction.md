Java Libraries 為 AutoJs6 腳本提供 Apache Commons Lang3, Apache Commons IO 與 Jackson 三個 Java 庫. 啟用後腳本照常以全名使用其中的類, 寫法與 AutoJs6 6.7.0 及更早版本相同.

### 開始使用

1. 確認 AutoJs6 插件中心裏本插件已啟用.
2. 在腳本中以全名使用庫中的類, 例如 `org.apache.commons.io.IOUtils`, 示例見下文.
3. 各庫的版本, Maven 構件與 Java 包可以在插件頁面查看; 插件頁面從插件中心或桌面上的 Java Libraries 圖示開啟.

### 主要功能

- **庫**: 插件頁面列出 Apache Commons Lang3, Apache Commons IO 與 Jackson 的版本, Maven 構件, Java 包與授權.
- **在腳本中使用**: 插件頁面提供一段可以直接運行的示例, 注釋是各行的輸出.
- **NOTICE 檔案**: 插件頁面顯示各庫發佈的 NOTICE 檔案原文.

### 在腳本中使用

以全名使用其中的類, 注釋為各行的輸出:

```js
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

- 也可以先 `importClass(org.apache.commons.lang3.StringUtils)`, 之後直接寫 `StringUtils`.
- 插件不可用時, 只引用類名不會立即出錯 (Rhino 把找不到的名字當作 Java 包), 調用方法, 構造對象或 `importClass` 時才出錯.

### 運行要求

- Android 7.0 或更高版本, AutoJs6 6.8.1 或更高版本.
- 本插件只是資源包, 腳本運行時不啟動插件進程, 也不需要任何權限; 不引用這些類的腳本不受影響.

### 常見問題

**腳本無法使用這些類時的處理**

確認本插件已安裝並在插件中心啟用, 且 AutoJs6 為 6.8.1 或更高版本. 腳本因此出錯結束時, AutoJs6 會在控制台的錯誤信息之後說明需要安裝, 授權, 啟用還是更新插件; 腳本自己 `catch` 的錯誤不附這段說明.

**在打包應用中使用**

用 AutoJs6 打包時, 腳本中出現以 `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` 開頭的名字會自動帶入本插件的類, 掃描不到時可以在打包頁的庫列表中勾選 Java Libraries. 帶入後的應用在未安裝本插件的裝置上也能使用這些類; 沒有帶入時使用裝置上已安裝的本插件.

源碼與第三方授權見 [項目主頁](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
