Java Libraries 는 AutoJs6 스크립트에 Apache Commons Lang3, Apache Commons IO, Jackson 세 가지 Java 라이브러리를 제공합니다. 활성화하면 스크립트에서 이 라이브러리의 클래스를 예전처럼 전체 이름으로 사용할 수 있으며, 작성 방법은 AutoJs6 6.7.0 이하와 같습니다.

### 시작하기

1. AutoJs6 플러그인 센터에서 이 플러그인이 활성화되어 있는지 확인합니다.
2. 스크립트에서 라이브러리의 클래스를 전체 이름으로 사용합니다. 예를 들면 `org.apache.commons.io.IOUtils` 입니다. 예제는 아래를 참고하세요.
3. 각 라이브러리의 버전, Maven 아티팩트, Java 패키지는 플러그인 페이지에서 볼 수 있습니다. 플러그인 페이지는 플러그인 센터 또는 홈 화면의 Java Libraries 아이콘으로 엽니다.

### 주요 기능

- **라이브러리**: 플러그인 페이지에 Apache Commons Lang3, Apache Commons IO, Jackson 의 버전, Maven 아티팩트, Java 패키지, 라이선스를 보여 줍니다.
- **스크립트에서 사용**: 플러그인 페이지에 그대로 실행할 수 있는 예제가 있으며, 주석은 각 줄의 출력입니다.
- **NOTICE 파일**: 플러그인 페이지에서 각 라이브러리가 배포한 NOTICE 파일 원문을 보여 줍니다.

### 스크립트에서 사용하기

클래스를 전체 이름으로 사용합니다. 주석은 각 줄의 출력입니다:

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

- 먼저 `importClass(org.apache.commons.lang3.StringUtils)` 를 호출하면 이후에는 `StringUtils` 만 써도 됩니다.
- 플러그인을 사용할 수 없을 때 클래스 이름을 참조하는 것만으로는 오류가 나지 않습니다 (Rhino 는 찾지 못한 이름을 Java 패키지로 취급합니다). 메서드를 호출하거나, 객체를 생성하거나, `importClass` 할 때 오류가 납니다.

### 요구 사항

- Android 7.0 이상, AutoJs6 6.8.1 이상.
- 이 플러그인은 리소스 패키지일 뿐이며, 스크립트 실행 중에 프로세스가 시작되지 않고 권한도 필요하지 않습니다. 이 클래스들을 참조하지 않는 스크립트는 영향을 받지 않습니다.

### 자주 묻는 질문

**스크립트에서 이 클래스들을 사용할 수 없을 때**

이 플러그인이 설치되어 플러그인 센터에서 활성화되어 있는지, AutoJs6 가 6.8.1 이상인지 확인하세요. 스크립트가 이런 오류로 끝나면 AutoJs6 는 콘솔의 오류 다음에 플러그인을 설치, 승인, 활성화, 업데이트 중 무엇을 해야 하는지 안내합니다. 스크립트가 직접 `catch` 한 오류에는 안내가 붙지 않습니다.

**패키징한 앱에서 사용하기**

AutoJs6 로 패키징할 때 스크립트에 `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` 로 시작하는 이름이 있으면 이 플러그인의 클래스를 자동으로 함께 넣습니다. 검사로 찾지 못하는 경우 패키징 페이지의 라이브러리 목록에서 Java Libraries 를 선택합니다. 클래스를 함께 넣은 앱은 이 플러그인이 없는 기기에서도 이 클래스들을 사용할 수 있고, 넣지 않았으면 기기에 설치된 이 플러그인을 사용합니다.

소스 코드와 서드파티 라이선스는 [프로젝트 페이지](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries) 에 있습니다.
