<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>AutoJs6 스크립트에서 Java 클래스로 바로 쓸 수 있는 Apache Commons Lang3, Commons IO, Jackson</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Java-Libraries?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 언어 (Languages)

******

현재 README.md 는 다음 언어를 지원합니다:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ja.md)
- 한국어 [ko] # 현재
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ar.md)

******

### 소개

******

Java Libraries 는 예전에 AutoJs6 에 내장되어 있던 세 Java 라이브러리, Apache Commons Lang3, Apache Commons IO, Jackson 을 기기에 되돌려 놓습니다. AutoJs6 6.8.0 부터 이 라이브러리들은 AutoJs6 패키지에 포함되지 않습니다. AutoJs6 6.8.1 이상에서 이 플러그인을 설치하고 활성화하면, 스크립트는 `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` 같은 클래스를 전체 이름으로 그대로 사용할 수 있으며, 작성법은 AutoJs6 6.7.0 이하와 같습니다.

플러그인 자체는 리소스 패키지일 뿐입니다. AutoJs6 가 플러그인 패키지에서 직접 클래스를 불러오므로, 스크립트 실행 중에 플러그인 프로세스가 시작되지 않고 플러그인은 어떤 권한도 요청하지 않습니다. 이 클래스들을 참조하지 않는 스크립트는 영향을 받지 않으며, 쓰이지 않는 클래스는 불러오지 않고 패키지 용량만 차지합니다.

******

### 포함된 라이브러리

******

각 라이브러리의 클래스는 아래 표의 Java 패키지에 있으며, 버전은 AutoJs6 6.7.0 에 내장되어 있던 것과 같습니다. 각 라이브러리의 라이선스와 NOTICE 파일은 [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md) 에 있고, 플러그인 페이지에도 표시됩니다.

| 라이브러리 | Maven 아티팩트와 버전 | Java 패키지 | 라이선스 |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### 사용 방법

******

1. AutoJs6 6.8.1 (빌드 6640) 이상을 사용합니다.
2. AutoJs6 의 플러그인 센터에서 Java Libraries 를 설치하고 ([Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases) 에서 APK 를 내려받아 설치해도 됩니다), 플러그인이 활성화되어 있는지 확인합니다.
3. 스크립트에서 라이브러리의 클래스를 전체 이름으로 사용합니다 (먼저 `importClass` 나 `importPackage` 를 써도 됩니다). 예:

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

> 패키지는 네이티브 라이브러리가 없는 범용 패키지 하나뿐이며, Android 7.0 (API 24) 이상의 기기를 지원합니다.

******

### 패키징한 앱

******

AutoJs6 로 스크립트를 독립 앱으로 패키징할 때 이 플러그인의 클래스를 함께 넣을 수 있으며, 패키징한 앱은 이 플러그인이 없는 기기에서도 라이브러리를 쓸 수 있습니다:

- 패키징할 때 AutoJs6 는 프로젝트의 스크립트를 검사하여 `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` 로 시작하는 이름을 찾으면 자동으로 함께 넣습니다 (암호화된 스크립트와 4 MB 를 넘는 파일은 검사하지 않습니다).
- 이름을 문자열을 이어 붙여 만들어서 검사로 찾지 못하는 경우, 패키징 페이지의 라이브러리 목록에서 Java Libraries 를 선택합니다.
- 함께 넣으려면 패키징하는 기기에 이 플러그인이 설치되어 활성화되어 있어야 합니다. 넣을 수 없으면 패키징은 그대로 완료되고, 완료 대화상자에 넣지 못한 플러그인과 해결 방법이 표시됩니다.
- 패키징한 앱은 함께 넣은 사본을 먼저 사용하고, 넣지 않았으면 기기에 설치된 이 플러그인을 사용합니다.

******

### 플러그인을 사용할 수 없을 때

******

플러그인이 설치되지 않았거나, 승인되지 않았거나, 플러그인 센터에서 비활성화되었거나, 버전이 너무 오래되었으면 스크립트는 이 클래스들을 사용할 수 없습니다:

- 클래스 이름을 참조하는 것만으로는 오류가 나지 않습니다 (Rhino 는 찾지 못한 이름을 Java 패키지로 취급합니다). 메서드를 호출하거나, 객체를 생성하거나, `importClass` 할 때 오류가 납니다.
- 스크립트가 이런 오류로 끝나면 AutoJs6 는 콘솔의 오류 다음에 플러그인을 설치, 승인, 활성화, 업데이트 중 무엇을 해야 하는지 안내합니다. 스크립트가 직접 `catch` 한 오류에는 안내가 붙지 않습니다.
- 패키징한 앱에 함께 넣은 사본이 없고 기기에도 쓸 수 있는 플러그인이 없으면, 안내는 기기에 이 플러그인을 설치하고 활성화하라고 알려 줍니다.

******

### 호환성

******

- AutoJs6 6.8.1 (빌드 6640) 이상이 필요합니다. AutoJs6 6.7.0 이하는 이 라이브러리들을 내장하고 있어 이 플러그인이 필요 없으며, AutoJs6 6.8.0 에서는 이 플러그인을 쓸 수 없습니다.
- Android 7.0 (API 24) 이상을 지원하며, 패키지 하나로 모든 프로세서 아키텍처를 지원합니다.
- 라이브러리 버전이 AutoJs6 6.7.0 에 내장되어 있던 것과 같으므로 기존 스크립트를 고칠 필요가 없습니다.
- AutoJs6 6.7.0 과 마찬가지로 이 라이브러리들에는 core library desugaring 이 적용되어 있습니다. 매개변수나 반환값이 `java.time` 타입인 메서드는 desugaring 된 `j$.time` 타입을 사용합니다.
- `java.lang.Class.forName` 으로 이름을 찾으면 이 플러그인의 클래스를 찾지 못합니다. 전체 이름으로 참조하거나 `importClass` 를 사용하세요.

******

### 패키지 크기

******

release 패키지는 약 1.0 MB 입니다. 세 라이브러리의 jar 는 합계 약 2.9 MB 이며, 패키지는 모든 클래스를 원래 이름 그대로 유지하지만 dex 의 공유 상수 풀과 압축 덕분에 더 작습니다.

******

### 자주 묻는 질문

******

#### AutoJs6 는 왜 이 라이브러리들을 더 이상 내장하지 않나요?

이 라이브러리를 쓰는 스크립트는 일부뿐입니다. 플러그인으로 옮기면 AutoJs6 패키지가 작아지고, 필요한 사람은 플러그인을 설치하면 됩니다. 플러그인의 라이브러리는 AutoJs6 6.7.0 에 내장되어 있던 것과 같은 버전입니다.

#### 왜 라이브러리를 새 버전으로 올리지 않나요?

새 버전은 더 이상 Android 7.x 를 지원하지 않습니다. Jackson 은 2.14 부터 Android 8.0 을 요구하고, 더 새로운 Commons IO 는 Android 7.x 에서 오류가 납니다. AutoJs6 가 지원하는 모든 기기에서 쓸 수 있고 기존 스크립트와 같게 동작하도록 AutoJs6 6.7.0 의 버전을 그대로 씁니다.

#### 플러그인이 인터넷에 연결하거나 백그라운드에서 실행되나요?

아니요. 플러그인은 어떤 권한도 요청하지 않으며, 스크립트를 실행해도 플러그인 프로세스가 시작되지 않습니다. 클래스는 스크립트를 실행하는 AutoJs6 안에서 실행됩니다.

#### 플러그인을 승인해야 한다는 오류가 나오면 어떻게 하나요?

AutoJs6 플러그인 센터나 이 프로젝트의 Releases 에서 설치한 정식 버전은 승인이 필요 없습니다. 직접 빌드한 버전은 서명이 다르므로 먼저 플러그인 센터에서 안내에 따라 승인해야 합니다.

#### AutoJs6 가 더 이상 내장하지 않는 다른 라이브러리 (ICU4J 등) 도 이 플러그인에 들어오나요?

당분간은 아닙니다. ICU4J 는 매우 크고 대부분의 용도는 시스템의 `android.icu` 로 대체할 수 있으며, Log4j 1.x 는 유지 관리가 끝났습니다. 스크립트가 이 라이브러리들을 쓰면 AutoJs6 가 더 이상 내장하지 않는다고 설명합니다.

******

### 플러그인 인터페이스

******

다음 정보는 AutoJs6 호스트와 플러그인 개발자를 위한 것이며, 호스트는 이 식별자로 플러그인을 찾고 그 클래스를 불러옵니다:

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

### 개발 로드맵

******

출시 전 점검 목록, 결정 사항, 출시 절차는 ROADMAP.md 에 기록되어 있습니다. 호스트가 이 클래스들을 불러오는 방식은 AutoJs6 저장소의 `org.autojs.autojs.core.plugin.javapackage` 패키지에 있습니다.

- [ROADMAP.md 보기](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### 릴리스 기록

******

#### v1.0.0

_2026/11/05_

- `힌트` AutoJs6 6.8.1 (빌드 6640) 이상이 필요합니다
- `힌트` AutoJs6 6.8.0 부터 Apache Commons Lang3, Commons IO, Jackson 이 더 이상 내장되지 않습니다. 이 플러그인을 설치하고 활성화하면 스크립트는 AutoJs6 6.7.0 과 같이 이 라이브러리들의 클래스를 전체 이름으로 사용하며, 기존 스크립트를 고칠 필요가 없습니다
- `기능` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0, Jackson 2.13.4.2 (core 와 annotations 는 2.13.4). 버전은 AutoJs6 6.7.0 에 내장되어 있던 것과 같습니다
- `기능` 플러그인은 리소스 패키지일 뿐입니다. AutoJs6 가 플러그인에서 직접 클래스를 불러오므로 스크립트를 실행해도 플러그인 프로세스가 시작되지 않고, 플러그인은 어떤 권한도 요청하지 않습니다
- `기능` AutoJs6 로 패키징한 앱에 이 클래스들을 함께 넣을 수 있으며 (스크립트 검사로 찾거나 라이브러리 목록에서 선택), 넣은 앱은 플러그인이 없는 기기에서도 사용할 수 있습니다
- `기능` 플러그인 페이지에 각 라이브러리의 버전, 라이선스, Java 패키지, 사용 예, NOTICE 파일, 릴리스 기록을 표시합니다
- `기능` 플러그인 페이지는 AutoJs6 의 언어, 야간 모드, 테마 색상을 따르며, AutoJs6 가 설치되지 않았거나 설정을 읽을 수 없으면 시스템을 따릅니다
- `의존성` AutoJs6 Common Plugin API 추가
- `의존성` desugar_jdk_libs_nio 2.1.5 추가

##### 더 많은 릴리스 기록은 다음을 참고

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-ko.md)

******

### 빌드

******

이 절은 소스에서 플러그인을 빌드하려는 개발자를 위한 것입니다.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### 라이선스

******

플러그인 코드는 [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE) 을 따릅니다. 포함된 세 라이브러리도 Apache License 2.0 을 따르며, 그 NOTICE 파일은 [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md) 에 있습니다. 패키지에는 Android 의 core library desugaring 런타임 (GNU GPL v2 와 Classpath Exception) 도 포함되어 있습니다.

******

### 관련 링크

******

- AutoJs6 프로젝트: https://github.com/SuperMonster003/AutoJs6
- 서드파티 고지: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
