Java Libraries brings back the three Java libraries that used to be built into AutoJs6: Apache Commons Lang3, Apache Commons IO and Jackson. Starting with AutoJs6 6.8.0, the AutoJs6 package no longer includes them; with this plugin installed and enabled in AutoJs6 6.8.1 or later, scripts use classes such as `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils` and `com.fasterxml.jackson.databind.ObjectMapper` by their full names, exactly as with AutoJs6 6.7.0 and earlier.

The plugin is only a resource package: AutoJs6 loads the classes straight from the plugin package, scripts never start the plugin process, and the plugin asks for no permission. Scripts that do not refer to these classes are not affected, and classes nobody uses are never loaded; they only take up space in the package.

| Library | Maven artifacts and versions | Java package | License |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

### Usage

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

### When the plugin is unavailable

When the plugin is not installed, not authorized, disabled in the Plugin Center or too old, scripts cannot use these classes:

- Naming a class is not an error by itself (Rhino treats a name it cannot find as a Java package); the error comes when a script calls a method, constructs an object or passes the name to `importClass`.
- When a script ends with such an error, AutoJs6 prints guidance after the error in the console, saying whether to install, authorize, enable or update the plugin; errors the script catches itself get no guidance.
- When a packaged app carries no copy and its device has no usable plugin either, the guidance says to install and enable the plugin on the device.

The source code, release history and third-party licenses are on the [project page](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
