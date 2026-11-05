# Third-party notices

The APK of this plugin contains the classes of the libraries below, unchanged (R8 keeps every class and member under its original name, ROADMAP.md D10). Each library is licensed under the Apache License 2.0; the full text is in [LICENSE](LICENSE) (the plugin itself uses the same license). The NOTICE files the libraries ship are reproduced below as published, and the launch page of the plugin shows them as well.

本插件的 APK 原样包含下列库的类 (R8 以原名保留全部类与成员, ROADMAP.md D10). 各库均以 Apache License 2.0 授权, 全文见 [LICENSE](LICENSE) (插件本身使用同一许可证). 各库随附的 NOTICE 文件按原文转载于下, 插件启动页也会显示.

## Libraries / 库

| Library | Maven artifact | Version | License | Project |
|---|---|---|---|---|
| Apache Commons Lang | `org.apache.commons:commons-lang3` | 3.18.0 | Apache-2.0 | https://commons.apache.org/proper/commons-lang/ |
| Apache Commons IO | `commons-io:commons-io` | 2.8.0 | Apache-2.0 | https://commons.apache.org/proper/commons-io/ |
| Jackson Databind | `com.fasterxml.jackson.core:jackson-databind` | 2.13.4.2 | Apache-2.0 | https://github.com/FasterXML/jackson-databind |
| Jackson Core | `com.fasterxml.jackson.core:jackson-core` | 2.13.4 | Apache-2.0 | https://github.com/FasterXML/jackson-core |
| Jackson Annotations | `com.fasterxml.jackson.core:jackson-annotations` | 2.13.4 | Apache-2.0 | https://github.com/FasterXML/jackson-annotations |

The APK also contains the core library desugaring runtime of Android (`com.android.tools:desugar_jdk_libs_nio` 2.1.5, GNU GPL v2 with the Classpath Exception, https://github.com/google/desugar_jdk_libs), which lets these libraries use `java.time` and `java.nio.file` on Android 7.x (ROADMAP.md D11).

APK 另含 Android 的 core library desugaring 运行库 (`com.android.tools:desugar_jdk_libs_nio` 2.1.5, GNU GPL v2 附 Classpath Exception, https://github.com/google/desugar_jdk_libs), 使这些库在 Android 7.x 上也能使用 `java.time` 与 `java.nio.file` (ROADMAP.md D11).

## NOTICE files / NOTICE 文件

### Apache Commons Lang 3.18.0 (`META-INF/NOTICE.txt`)

```text
Apache Commons Lang
Copyright 2001-2025 The Apache Software Foundation

This product includes software developed at
The Apache Software Foundation (https://www.apache.org/).
```

### Apache Commons IO 2.8.0 (`META-INF/NOTICE.txt`)

```text
Apache Commons IO
Copyright 2002-2020 The Apache Software Foundation

This product includes software developed at
The Apache Software Foundation (https://www.apache.org/).
```

### Jackson Databind 2.13.4.2 and Jackson Core 2.13.4 (`META-INF/NOTICE`, the same text in both)

```text
# Jackson JSON processor

Jackson is a high-performance, Free/Open Source JSON processing library.
It was originally written by Tatu Saloranta (tatu.saloranta@iki.fi), and has
been in development since 2007.
It is currently developed by a community of developers.

## Licensing

Jackson 2.x core and extension components are licensed under Apache License 2.0
To find the details that apply to this artifact see the accompanying LICENSE file.

## Credits

A list of contributors may be found from CREDITS(-2.x) file, which is included
in some artifacts (usually source distributions); but is always available
from the source code management (SCM) system project uses.
```

Jackson Annotations 2.13.4 ships no NOTICE file.

Jackson Annotations 2.13.4 未随附 NOTICE 文件.
