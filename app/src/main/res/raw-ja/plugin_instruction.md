Java Libraries は AutoJs6 のスクリプトに Apache Commons Lang3, Apache Commons IO, Jackson の 3 つの Java ライブラリを提供します. 有効にすると, スクリプトはこれまでどおりそのクラスを完全名で使えます. 書き方は AutoJs6 6.7.0 以前と同じです.

### はじめに

1. AutoJs6 のプラグインセンターでこのプラグインが有効になっていることを確認します.
2. スクリプトでライブラリのクラスを完全名で使います. 例えば `org.apache.commons.io.IOUtils` です. 例は下記を参照してください.
3. 各ライブラリのバージョン, Maven アーティファクト, Java パッケージはプラグインのページで確認できます. このページはプラグインセンター, またはホーム画面の Java Libraries アイコンから開きます.

### 主な機能

- **ライブラリ**: プラグインのページに Apache Commons Lang3, Apache Commons IO, Jackson のバージョン, Maven アーティファクト, Java パッケージ, ライセンスを一覧表示します.
- **スクリプトでの使い方**: プラグインのページにそのまま実行できる例があり, コメントは各行の出力です.
- **NOTICE ファイル**: プラグインのページに各ライブラリが公開している NOTICE ファイルの原文を表示します.

### スクリプトでの使い方

クラスを完全名で使います. コメントは各行の出力です:

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

- 先に `importClass(org.apache.commons.lang3.StringUtils)` を呼べば, その後は `StringUtils` とだけ書けます.
- プラグインが使えないとき, クラス名を参照しただけではエラーになりません (Rhino は見つからない名前を Java パッケージとして扱います). メソッドの呼び出し, オブジェクトの生成, `importClass` の時点でエラーになります.

### 動作要件

- Android 7.0 以降, AutoJs6 6.8.1 以降.
- このプラグインはリソースパッケージにすぎず, スクリプトの実行中にプロセスが起動することはなく, 権限も必要ありません. これらのクラスを参照しないスクリプトには影響がありません.

### よくある質問

**スクリプトでこれらのクラスが使えない場合**

このプラグインがインストールされ, プラグインセンターで有効になっていること, AutoJs6 が 6.8.1 以降であることを確認してください. スクリプトがこの種のエラーで終了すると, AutoJs6 はコンソールのエラーの後に, プラグインのインストール, 承認, 有効化, 更新のどれが必要かを案内します. スクリプト自身が `catch` したエラーには案内が付きません.

**パッケージ化したアプリでの利用**

AutoJs6 でパッケージ化する際, スクリプトに `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` で始まる名前があれば, このプラグインのクラスを自動で取り込みます. 走査で見つからない場合は, パッケージ化ページのライブラリ一覧で Java Libraries にチェックを入れます. 取り込んだアプリはこのプラグインがない端末でもこれらのクラスを使え, 取り込んでいない場合は端末にインストールされたこのプラグインを使います.

ソースコードとサードパーティのライセンスは [プロジェクトページ](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries) にあります.
