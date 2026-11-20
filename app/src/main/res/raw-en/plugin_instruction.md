Java Libraries provides three Java libraries for AutoJs6 scripts: Apache Commons Lang3, Apache Commons IO and Jackson. Once it is enabled, scripts keep using their classes by full name, just as in AutoJs6 6.7.0 and earlier.

### Getting started

1. Make sure this plugin is enabled in the Plugin Center of AutoJs6.
2. In a script, use the classes of the libraries by their full names, for example `org.apache.commons.io.IOUtils`; see the example below.
3. The plugin page shows the version, Maven artifacts and Java packages of each library; it opens from the Plugin Center or the Java Libraries icon on the home screen.

### Main features

- **Libraries**: the plugin page lists the version, Maven artifacts, Java packages and license of Apache Commons Lang3, Apache Commons IO and Jackson.
- **Use in scripts**: the plugin page shows an example you can run as is, with the output of each line in comments.
- **NOTICE files**: the plugin page shows the NOTICE files as the libraries publish them.

### Using it in scripts

Use the classes by their full names; the comments show the output of each line:

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

- You can also call `importClass(org.apache.commons.lang3.StringUtils)` first and then write just `StringUtils`.
- When the plugin is unavailable, naming a class is not an error by itself (Rhino treats a name it cannot find as a Java package); the error comes when a script calls a method, constructs an object or passes the name to `importClass`.

### Requirements

- Android 7.0 or later, and AutoJs6 6.8.1 or later.
- This plugin is only a resource package: scripts never start its process, and it needs no permissions; scripts that do not refer to these classes are not affected.

### FAQ

**A script cannot use these classes**

Check that this plugin is installed and enabled in the Plugin Center, and that AutoJs6 is 6.8.1 or later. When a script ends with such an error, AutoJs6 says after the error in the console whether to install, authorize, enable or update the plugin; errors the script handles itself with `catch` get no such note.

**Using them in packaged apps**

When AutoJs6 packages a project, it takes the classes of this plugin along if the scripts contain a name that starts with `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson`; when the scan cannot find such a name, tick Java Libraries in the list of libraries on the packaging page. An app that carries them can use these classes on devices without this plugin; otherwise it uses the plugin installed on the device.

Source code and third-party licenses are on the [project page](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
