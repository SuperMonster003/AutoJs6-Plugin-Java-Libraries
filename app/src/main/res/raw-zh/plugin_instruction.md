Java Libraries 把 AutoJs6 过去内置的三个 Java 库装回设备: Apache Commons Lang3, Apache Commons IO 与 Jackson. 自 AutoJs6 6.8.0 起, 这些库不再随 AutoJs6 安装包提供; 在 AutoJs6 6.8.1 或更高版本中安装并启用本插件后, 脚本照常以全名使用 `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils`, `com.fasterxml.jackson.databind.ObjectMapper` 等类, 写法与 AutoJs6 6.7.0 及更早版本相同.

插件本身只是一个资源包: AutoJs6 直接从插件的安装包加载这些类, 脚本运行时不会启动插件进程, 插件也不申请任何权限. 不引用这些类的脚本不受影响, 用不到的类不会被加载, 只占用安装包体积.

| 库 | Maven 构件与版本 | Java 包 | 许可证 |
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

### 插件不可用时

插件未安装, 未授权, 在插件中心被停用, 或版本过旧时, 脚本无法使用这些类:

- 引用类名本身不会立即出错 (Rhino 把找不到的名字当作 Java 包), 调用方法, 构造对象或 `importClass` 时才出错.
- 脚本因这类错误结束时, AutoJs6 在控制台的错误信息之后给出指引, 说明需要安装, 授权, 启用还是更新插件; 脚本自己 `catch` 的错误不附指引.
- 打包应用既没有带入的副本, 所在设备上也没有可用的本插件时, 指引会说明需要在设备上安装并启用本插件.

源码, 发行历史与第三方许可证见 [项目主页](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
