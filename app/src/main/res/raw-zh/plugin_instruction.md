Java Libraries 为 AutoJs6 脚本提供 Apache Commons Lang3, Apache Commons IO 与 Jackson 三个 Java 库. 启用后脚本照常以全名使用其中的类, 写法与 AutoJs6 6.7.0 及更早版本相同.

### 开始使用

1. 确认 AutoJs6 插件中心里本插件已启用.
2. 在脚本中以全名使用库中的类, 例如 `org.apache.commons.io.IOUtils`, 示例见下文.
3. 各库的版本, Maven 构件与 Java 包可以在插件页面查看; 插件页面从插件中心或桌面上的 Java Libraries 图标打开.

### 主要功能

- **库**: 插件页面列出 Apache Commons Lang3, Apache Commons IO 与 Jackson 的版本, Maven 构件, Java 包与许可证.
- **在脚本中使用**: 插件页面给出一段可以直接运行的示例, 注释是各行的输出.
- **NOTICE 文件**: 插件页面显示各库发布的 NOTICE 文件原文.

### 在脚本中使用

以全名使用其中的类, 注释为各行的输出:

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

- 也可以先 `importClass(org.apache.commons.lang3.StringUtils)`, 之后直接写 `StringUtils`.
- 插件不可用时, 只引用类名不会立即出错 (Rhino 把找不到的名字当作 Java 包), 调用方法, 构造对象或 `importClass` 时才出错.

### 运行要求

- Android 7.0 或更高版本, AutoJs6 6.8.1 或更高版本.
- 本插件只是资源包, 脚本运行时不启动插件进程, 也不需要任何权限; 不引用这些类的脚本不受影响.

### 常见问题

**脚本无法使用这些类时的处理**

确认本插件已安装并在插件中心启用, 且 AutoJs6 为 6.8.1 或更高版本. 脚本因此出错结束时, AutoJs6 会在控制台的错误信息之后说明需要安装, 授权, 启用还是更新插件; 脚本自己 `catch` 的错误不附这段说明.

**在打包应用中使用**

用 AutoJs6 打包时, 脚本中出现以 `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` 开头的名字会自动带入本插件的类, 扫描不到时可以在打包页的库列表中勾选 Java Libraries. 带入后的应用在未安装本插件的设备上也能使用这些类; 没有带入时使用设备上已安装的本插件.

源码与第三方许可证见 [项目主页](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
