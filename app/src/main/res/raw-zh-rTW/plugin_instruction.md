Java Libraries 為 AutoJs6 腳本提供 Apache Commons Lang3, Apache Commons IO 與 Jackson 三個 Java 函式庫. 啟用後腳本照常以全名使用其中的類別, 寫法與 AutoJs6 6.7.0 及更早版本相同.

### 開始使用

1. 確認 AutoJs6 外掛中心裡本外掛已啟用.
2. 在腳本中以全名使用函式庫中的類別, 例如 `org.apache.commons.io.IOUtils`, 範例見下文.
3. 各函式庫的版本, Maven 構件與 Java 套件可以在外掛頁面查看; 外掛頁面從外掛中心或桌面上的 Java Libraries 圖示開啟.

### 主要功能

- **函式庫**: 外掛頁面列出 Apache Commons Lang3, Apache Commons IO 與 Jackson 的版本, Maven 構件, Java 套件與授權.
- **在腳本中使用**: 外掛頁面提供一段可以直接執行的範例, 註解是各行的輸出.
- **NOTICE 檔案**: 外掛頁面顯示各函式庫發布的 NOTICE 檔案原文.

### 在指令碼中使用

以全名使用其中的類別, 註解為各行的輸出:

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
- 外掛無法使用時, 只引用類別名稱不會立即出錯 (Rhino 把找不到的名稱當作 Java 套件), 呼叫方法, 建構物件或 `importClass` 時才出錯.

### 執行需求

- Android 7.0 或更新版本, AutoJs6 6.8.1 或更新版本.
- 本外掛只是資源套件, 腳本執行時不啟動外掛處理程序, 也不需要任何權限; 不引用這些類別的腳本不受影響.

### 常見問題

**腳本無法使用這些類別時的處理**

確認本外掛已安裝並在外掛中心啟用, 且 AutoJs6 為 6.8.1 或更新版本. 腳本因此出錯結束時, AutoJs6 會在主控台的錯誤訊息之後說明需要安裝, 授權, 啟用還是更新外掛; 腳本自己 `catch` 的錯誤不附這段說明.

**在打包的應用程式中使用**

用 AutoJs6 打包時, 腳本中出現以 `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` 開頭的名稱會自動帶入本外掛的類別, 掃描不到時可以在打包頁面的函式庫清單中勾選 Java Libraries. 帶入後的應用程式在未安裝本外掛的裝置上也能使用這些類別; 沒有帶入時使用裝置上已安裝的本外掛.

原始碼與第三方授權見 [專案首頁](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
