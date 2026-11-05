<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>AutoJs6 スクリプトから Java クラスとして直接使える Apache Commons Lang3, Commons IO, Jackson</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Java-Libraries?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 言語 (Languages)

******

現在の README.md は次の言語に対応しています:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-es.md)
- 日本語 [ja] # 現在
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ar.md)

******

### 概要

******

Java Libraries は, かつて AutoJs6 に内蔵されていた 3 つの Java ライブラリ, Apache Commons Lang3, Apache Commons IO, Jackson を端末に戻します. AutoJs6 6.8.0 以降, これらのライブラリは AutoJs6 のパッケージに含まれていません. AutoJs6 6.8.1 以降でこのプラグインをインストールして有効にすると, スクリプトは `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` などのクラスを完全名でこれまでどおり使えます. 書き方は AutoJs6 6.7.0 以前と同じです.

プラグイン自体はリソースパッケージにすぎません. AutoJs6 はプラグインのパッケージから直接クラスを読み込み, スクリプトの実行中にプラグインのプロセスが起動することはなく, プラグインは権限を一切要求しません. これらのクラスを参照しないスクリプトには影響がなく, 使われないクラスは読み込まれず, パッケージの容量を占めるだけです.

******

### 収録ライブラリ

******

各ライブラリのクラスは下表の Java パッケージにあり, バージョンは AutoJs6 6.7.0 に内蔵されていたものと同じです. 各ライブラリのライセンスと NOTICE ファイルは [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md) にあり, プラグインのページにも表示されます.

| ライブラリ | Maven アーティファクトとバージョン | Java パッケージ | ライセンス |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### 使用方法

******

1. AutoJs6 6.8.1 (ビルド 6641) 以降を使います.
2. AutoJs6 のプラグインセンターから Java Libraries をインストールし ([Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases) から APK をダウンロードしても構いません), プラグインが有効になっていることを確認します.
3. スクリプトでライブラリのクラスを完全名で使います (先に `importClass` や `importPackage` を使っても構いません). 例:

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

> パッケージはネイティブライブラリを含まない汎用版の 1 つだけで, Android 7.0 (API 24) 以降の端末に対応します.

******

### パッケージ化したアプリ

******

AutoJs6 でスクリプトを独立したアプリにパッケージ化するとき, このプラグインのクラスを一緒に取り込めます. パッケージ化したアプリは, このプラグインがない端末でもこれらのライブラリを使えます:

- パッケージ化の際, AutoJs6 はプロジェクトのスクリプトを走査し, `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` で始まる名前を見つけると自動で取り込みます (暗号化されたスクリプトと 4 MB を超えるファイルは走査しません).
- 名前を文字列の連結で組み立てていて走査で見つからない場合は, パッケージ化ページのライブラリ一覧で Java Libraries にチェックを入れます.
- 取り込むには, パッケージ化に使う端末にこのプラグインがインストールされ有効になっている必要があります. 取り込めない場合もパッケージ化は完了し, 完了ダイアログに取り込めなかったプラグインと対処方法が表示されます.
- パッケージ化したアプリは取り込んだコピーを優先して使い, 取り込んでいない場合は端末にインストールされたこのプラグインを使います.

******

### プラグインを使えないとき

******

プラグインが未インストール, 未承認, プラグインセンターで無効, または古すぎる場合, スクリプトはこれらのクラスを使えません:

- クラス名を参照しただけではエラーになりません (Rhino は見つからない名前を Java パッケージとして扱います). メソッドの呼び出し, オブジェクトの生成, `importClass` の時点でエラーになります.
- スクリプトがこの種のエラーで終了すると, AutoJs6 はコンソールのエラーの後に, プラグインのインストール, 承認, 有効化, 更新のどれが必要かを案内します. スクリプト自身が `catch` したエラーには案内が付きません.
- パッケージ化したアプリが取り込んだコピーを持たず, 端末にも使えるプラグインがない場合, 案内は端末でこのプラグインをインストールして有効にするよう伝えます.

******

### 互換性

******

- AutoJs6 6.8.1 (ビルド 6641) 以降が必要です. AutoJs6 6.7.0 以前はこれらのライブラリを内蔵しているためこのプラグインは不要で, AutoJs6 6.8.0 ではこのプラグインを使えません.
- Android 7.0 (API 24) 以降に対応し, 1 つのパッケージですべてのプロセッサアーキテクチャに対応します.
- ライブラリのバージョンは AutoJs6 6.7.0 に内蔵されていたものと同じなので, 既存のスクリプトを変更する必要はありません.
- AutoJs6 6.7.0 と同じく, これらのライブラリには core library desugaring が適用されています. 引数や戻り値が `java.time` 型のメソッドは, desugaring 後の `j$.time` 型を使います.
- `java.lang.Class.forName` で名前から検索しても, このプラグインのクラスは見つかりません. 完全名で参照するか `importClass` を使ってください.

******

### パッケージサイズ

******

release パッケージは約 1.2 MB です. 3 つのライブラリの jar は合計約 2.9 MB で, パッケージではすべてのクラスを元の名前のまま保持していますが, dex の共有定数プールと圧縮により小さくなります.

******

### よくある質問

******

#### なぜ AutoJs6 はこれらのライブラリを内蔵しなくなったのですか?

これらのライブラリを使うスクリプトは一部だけです. プラグインに移すことで AutoJs6 のパッケージが小さくなり, 必要な人はプラグインをインストールすれば済みます. プラグインのライブラリは AutoJs6 6.7.0 に内蔵されていたものと同じバージョンです.

#### なぜライブラリを新しいバージョンに更新しないのですか?

新しいバージョンは Android 7.x に対応していません. Jackson は 2.14 から Android 8.0 を必要とし, より新しい Commons IO は Android 7.x でエラーになります. AutoJs6 が対応するすべての端末で使えるよう, また既存のスクリプトとの一貫性を保つため, AutoJs6 6.7.0 のバージョンを引き継いでいます.

#### プラグインはネットワークに接続したり, バックグラウンドで動いたりしますか?

しません. プラグインは権限を一切要求せず, スクリプトの実行時にプラグインのプロセスが起動することもありません. クラスはスクリプトを実行している AutoJs6 の中で動きます.

#### プラグインの承認が必要というエラーが出たらどうすればよいですか?

AutoJs6 のプラグインセンターまたはこのプロジェクトの Releases からインストールした正式版は承認不要です. 自分でビルドした版は署名が異なるため, 先にプラグインセンターで案内に従って承認してください.

#### AutoJs6 が内蔵しなくなったほかのライブラリ (ICU4J など) もこのプラグインに加わりますか?

今のところ予定はありません. ICU4J は非常に大きく, ほとんどの用途はシステムの `android.icu` で代替でき, Log4j 1.x はメンテナンスが終了しています. スクリプトがこれらのライブラリを使うと, AutoJs6 はもう内蔵していないことを説明します.

******

### プラグインインターフェース

******

以下の情報は AutoJs6 ホストとプラグインの開発者向けです. ホストはこれらの識別子でプラグインを見つけ, そのクラスを読み込みます:

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

### 開発ロードマップ

******

リリース前のチェックリスト, 決定事項, リリース手順は ROADMAP.md に記録しています. ホストがこれらのクラスを読み込む仕組みは AutoJs6 リポジトリの `org.autojs.autojs.core.plugin.javapackage` パッケージにあります.

- [ROADMAP.md を見る](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### リリース履歴

******

#### v1.0.0

_2026/11/05_

- `ヒント` AutoJs6 6.8.1 (ビルド 6641) 以降が必要です
- `ヒント` AutoJs6 6.8.0 以降, Apache Commons Lang3, Commons IO, Jackson は内蔵されなくなりました. このプラグインをインストールして有効にすると, スクリプトはこれらのライブラリのクラスを AutoJs6 6.7.0 と同じく完全名で使え, 既存のスクリプトを変更する必要はありません
- `機能` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0, Jackson 2.13.4.2 (core と annotations は 2.13.4). バージョンは AutoJs6 6.7.0 に内蔵されていたものと同じです
- `機能` プラグインはリソースパッケージにすぎません. AutoJs6 がプラグインから直接クラスを読み込み, スクリプトの実行でプラグインのプロセスが起動することはなく, プラグインは権限を一切要求しません
- `機能` AutoJs6 でパッケージ化したアプリにこれらのクラスを取り込めます (スクリプトの走査で検出するか, ライブラリ一覧でチェック). 取り込んだアプリはプラグインのない端末でも使えます
- `機能` プラグインのページに各ライブラリのバージョン, ライセンス, Java パッケージ, 使用例, NOTICE ファイル, リリース履歴を表示します
- `機能` プラグインのページは AutoJs6 の言語, ナイトモード, テーマカラーに従い, AutoJs6 が未インストールまたは設定を読めない場合はシステムに従います
- `依存関係` AutoJs6 Common Plugin API を追加
- `依存関係` desugar_jdk_libs_nio 2.1.5 を追加

##### その他のリリース履歴は以下を参照

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-ja.md)

******

### ビルド

******

この節はソースからプラグインをビルドしたい開発者向けです.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### ライセンス

******

プラグインのコードは [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE) です. 収録した 3 つのライブラリも Apache License 2.0 で, その NOTICE ファイルは [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md) にあります. パッケージには Android の core library desugaring ランタイム (GNU GPL v2 と Classpath Exception) も含まれます.

******

### 関連リンク

******

- AutoJs6 プロジェクト: https://github.com/SuperMonster003/AutoJs6
- サードパーティ通知: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
