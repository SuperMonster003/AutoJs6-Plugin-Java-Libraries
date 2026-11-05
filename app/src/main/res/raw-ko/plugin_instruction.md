Java Libraries 는 예전에 AutoJs6 에 내장되어 있던 세 Java 라이브러리, Apache Commons Lang3, Apache Commons IO, Jackson 을 기기에 되돌려 놓습니다. AutoJs6 6.8.0 부터 이 라이브러리들은 AutoJs6 패키지에 포함되지 않습니다. AutoJs6 6.8.1 이상에서 이 플러그인을 설치하고 활성화하면, 스크립트는 `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` 같은 클래스를 전체 이름으로 그대로 사용할 수 있으며, 작성법은 AutoJs6 6.7.0 이하와 같습니다.

플러그인 자체는 리소스 패키지일 뿐입니다. AutoJs6 가 플러그인 패키지에서 직접 클래스를 불러오므로, 스크립트 실행 중에 플러그인 프로세스가 시작되지 않고 플러그인은 어떤 권한도 요청하지 않습니다. 이 클래스들을 참조하지 않는 스크립트는 영향을 받지 않으며, 쓰이지 않는 클래스는 불러오지 않고 패키지 용량만 차지합니다.

| 라이브러리 | Maven 아티팩트와 버전 | Java 패키지 | 라이선스 |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

### 사용 방법

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

### 플러그인을 사용할 수 없을 때

플러그인이 설치되지 않았거나, 승인되지 않았거나, 플러그인 센터에서 비활성화되었거나, 버전이 너무 오래되었으면 스크립트는 이 클래스들을 사용할 수 없습니다:

- 클래스 이름을 참조하는 것만으로는 오류가 나지 않습니다 (Rhino 는 찾지 못한 이름을 Java 패키지로 취급합니다). 메서드를 호출하거나, 객체를 생성하거나, `importClass` 할 때 오류가 납니다.
- 스크립트가 이런 오류로 끝나면 AutoJs6 는 콘솔의 오류 다음에 플러그인을 설치, 승인, 활성화, 업데이트 중 무엇을 해야 하는지 안내합니다. 스크립트가 직접 `catch` 한 오류에는 안내가 붙지 않습니다.
- 패키징한 앱에 함께 넣은 사본이 없고 기기에도 쓸 수 있는 플러그인이 없으면, 안내는 기기에 이 플러그인을 설치하고 활성화하라고 알려 줍니다.

소스 코드, 릴리스 기록, 서드파티 라이선스는 [프로젝트 페이지](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries) 에 있습니다.
