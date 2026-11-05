Java Libraries は, かつて AutoJs6 に内蔵されていた 3 つの Java ライブラリ, Apache Commons Lang3, Apache Commons IO, Jackson を端末に戻します. AutoJs6 6.8.0 以降, これらのライブラリは AutoJs6 のパッケージに含まれていません. AutoJs6 6.8.1 以降でこのプラグインをインストールして有効にすると, スクリプトは `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` などのクラスを完全名でこれまでどおり使えます. 書き方は AutoJs6 6.7.0 以前と同じです.

プラグイン自体はリソースパッケージにすぎません. AutoJs6 はプラグインのパッケージから直接クラスを読み込み, スクリプトの実行中にプラグインのプロセスが起動することはなく, プラグインは権限を一切要求しません. これらのクラスを参照しないスクリプトには影響がなく, 使われないクラスは読み込まれず, パッケージの容量を占めるだけです.

| ライブラリ | Maven アーティファクトとバージョン | Java パッケージ | ライセンス |
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

### プラグインを使えないとき

プラグインが未インストール, 未承認, プラグインセンターで無効, または古すぎる場合, スクリプトはこれらのクラスを使えません:

- クラス名を参照しただけではエラーになりません (Rhino は見つからない名前を Java パッケージとして扱います). メソッドの呼び出し, オブジェクトの生成, `importClass` の時点でエラーになります.
- スクリプトがこの種のエラーで終了すると, AutoJs6 はコンソールのエラーの後に, プラグインのインストール, 承認, 有効化, 更新のどれが必要かを案内します. スクリプト自身が `catch` したエラーには案内が付きません.
- パッケージ化したアプリが取り込んだコピーを持たず, 端末にも使えるプラグインがない場合, 案内は端末でこのプラグインをインストールして有効にするよう伝えます.

ソースコード, リリース履歴, サードパーティのライセンスは [プロジェクトページ](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries) にあります.
