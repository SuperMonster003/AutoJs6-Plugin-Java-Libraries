******

### Release history

******

# v1.0.0

###### 2026/11/05

* `Hint` Requires AutoJs6 6.8.1 (build 6640) or later
* `Hint` Starting with AutoJs6 6.8.0, Apache Commons Lang3, Commons IO and Jackson are no longer built in; with this plugin installed and enabled, scripts use the classes of these libraries by their full names as with AutoJs6 6.7.0, and existing scripts need no change
* `Feature` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 and Jackson 2.13.4.2 (core and annotations 2.13.4), the versions AutoJs6 6.7.0 included
* `Feature` The plugin is only a resource package: AutoJs6 loads the classes straight from the plugin, running scripts never starts the plugin process, and the plugin asks for no permission
* `Feature` Apps packaged with AutoJs6 can carry these classes (found by scanning the scripts, or ticked in the list of libraries) and then use them on devices without the plugin
* `Feature` The plugin page shows the version, license and Java package of each library, an example, the NOTICE files and the release history
* `Feature` The plugin page follows the language, night mode and theme color of AutoJs6, and the system when AutoJs6 is not installed or its settings cannot be read
* `Dependency` Attach AutoJs6 Common Plugin API
* `Dependency` Attach desugar_jdk_libs_nio 2.1.5
