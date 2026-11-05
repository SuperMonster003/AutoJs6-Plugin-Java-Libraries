# Java Libraries 插件路线图

更新: 2026-11-05 (立项). 本文件是 `AutoJs6-Plugin-Java-Libraries` (显示名 `Java Libraries`) 的唯一进度与决策记录. 插件把 AutoJs6 6.8.0 移除的纯 Java 库 (Apache Commons Lang3, Commons IO, Jackson) 装回设备, 让脚本再次直接使用 `org.apache.commons.lang3.StringUtils`, `com.fasterxml.jackson.databind.ObjectMapper` 等类, 写法与 6.8.0 之前相同.

立项基线: 宿主 AutoJs6 `master` `e195d5c7f8` (2026-11-05, `VERSION_NAME` 6.8.1 未发布, `VERSION_BUILD` 6640). 宿主已有 "官方插件导出 Java 包" 机制 (本地提交 `392746d017`, 第 4 节), 试点为 Pinyin4j 1.4.0 (插件本地提交 `c82d82e`, 等宿主 6.8.1 发布). 证据: 宿主 `docs/dev/evidence/plugin-java-packages-pilot-20261103.md`.

使用方式:

- 每个条目完成时在条目前打勾 (`[x]`), 在条目末尾写提交 hash (插件仓库) 或 "宿主本地提交 `<hash>`" (宿主只落本地提交, 推送由维护者逐次决定). 阶段总览的状态列同步更新.
- 第 1 节是维护者经两轮选择题 (2026-11-04 至 2026-11-05) 选定的固定决策, 不再重问. 第 2 节是据此派生的技术决策, 进入对应阶段前可推翻, 推翻时在条目里写明理由并改写该行.
- 实现中遇到需要维护者拍板的事, 不阻塞的按推荐项先行, 登记到附录 D (编号, 问题, 选项, 推荐项, 是否已先行, 影响的条目, 改选返工量) 与看板 `D:/idea-projects/.claude/boards/java-libraries.md`; 阻塞的停下来用选择题问维护者.
- 每完成一个子阶段就把进度写进本文件, 不靠会话上下文记住.
- 条目前缀: (插件) 本仓库; (宿主) AutoJs6 仓库; (模板) AutoJs6-Plugin-APK-Builder-Template; (索引) AutoJs6-Official-Plugins-Index; (文档) AutoJs6-Documentation / Offline-Docs; (测试) 设备或 AVD 验证; (发布) 发布门禁.

---

## 1. 固定决策

| 编号 | 决策 | 来源 |
| --- | --- | --- |
| D1 | 收录 Apache Commons Lang3, Apache Commons IO, Jackson (core / annotations / databind). 不收 ICU4J (jar 14.3 MB, 系统 `android.icu` 可替代多数用法), Log4j 1.x (停止维护, 5 项已知漏洞), Flexmark, Spongy Castle, GitHub API, Android Device Names; 这些仍按 "已不再内置" 给说明 | 2026-11-04 Q1 |
| D2 | 版本沿用 6.7.0 宿主: `commons-lang3` 3.18.0, `commons-io` 2.8.0, `jackson-databind` 2.13.4.2 (随 `jackson-core` / `jackson-annotations` 2.13.4). Jackson 2.13 是支持 API 24 的最后一个系列 (2.14 起要求 API 26); Commons IO 停在 2.8.0 是宿主当年因新版在 Android 上 `NoClassDefFoundError` 而固定的版本 | 2026-11-04 Q2 |
| D3 | 新插件 `Java Libraries`, 新仓库 `AutoJs6-Plugin-Java-Libraries`. 纯资源包: 宿主从 APK 把类加载进自己的进程, 不启动插件进程, 插件不申请权限; 不设逐库开关 (不用的类不加载, 只占安装包体积) | 2026-11-04 Q3 |
| D4 | 打包应用支持带入: 打包时把插件的类带进打包应用, 运行时先用带入的副本 | 2026-11-04 Q4 |
| D5 | 打包的决定方式为 "勾选 + 自动识别": 扫描脚本里的导出包名, 用到即带入; 拼接名扫不到时可在打包页 "库" 里勾选 "Java Libraries" | 2026-11-05 Q5 |
| D6 | 执行方式: 立项后由调度会话 `/dispatch java-libraries` (从 dispatch 环境启动) 分配子会话 | 2026-11-05 Q6 |
| D7 | 发布时点: 插件依赖宿主 6.8.1 的导出机制, 与 Pinyin4j 1.4.0 一样等宿主 6.8.1 发布后再推送发布 (维护者 2026-11-04 对 Pinyin4j 的决定, 同一原因) | 沿用 |

## 2. 派生技术决策

| 编号 | 决策 | 推翻点 |
| --- | --- | --- |
| D8 | 身份见 5.1 节. engine `java-libraries`, 插件 ID `java-libraries`, 变体 `default`; 只有一个 `org.autojs.plugin.INFO` 服务 (category `java-libraries`) 供插件中心列表, 与 Bundled Libraries 的 `BundledLibrariesInfoService` 同构 | P0.2 前 |
| D9 | 导出声明: application meta-data `org.autojs.plugin.javaPackages.EXPORTS` = `org.apache.commons.lang3,org.apache.commons.io,com.fasterxml.jackson` (逗号分隔, 宿主 `PluginJavaPackageCatalog.parseExports`). 宿主登记表为这三个前缀各加一条 `Entry`, 同时从 `RETIRED` 删除这三条 | P1.1 前 |
| D10 | R8: release 保持 `isMinifyEnabled = true` (插件自身代码照常缩减), 对三个导出包 `-keep class <包>.** { *; }`, 并 `-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisible*Annotations, AnnotationDefault` (Jackson 读注解与泛型签名). 发布校验脚本按 附录 C的代表类清单检查签名 APK 的 dex 中原名都在 | P0.1 |
| D11 | 脱糖沿用宿主 6.7.0: core library desugaring (`desugar_jdk_libs_nio` 2.1.5), 使 `java.time` 等签名的行为与 6.7.0 一致. 代价: 这些库中用到 `java.time` 的方法收到的是 `j$.time` 类型, 与 6.7.0 相同 | P0.1, 若设备回归发现与 6.7.0 不一致再议 |
| D12 | 插件启动页: 列出三个库的版本, 许可证与导出包, 附发布历史与 "在脚本中使用" 示例, 跟随宿主外观 (复用 Bundled Libraries 的 `HostAppearance` 一套). 无设置项 | P0.4 前 |
| D13 | 打包载荷: 从已安装插件 APK 取出 `classes*.dex` 与 Java 资源 (排除 `res/`, `assets/`, `resources.arsc`, `AndroidManifest.xml`, `META-INF/` 签名文件), 打成一个 zip 放在打包应用 `assets/java-packages/<插件包名>.zip`, 另写 `assets/java-packages/<插件包名>.json` (导出包, 插件 versionName / versionCode, zip 的 SHA-256). 不复制整个 APK (省去图标与资源) | P2.1 前 |
| D14 | 打包应用运行时: `PluginJavaPackages` 在打包应用中先找带入的 zip, 首次使用时按 SHA-256 解出到 `noBackupFilesDir/java-packages/`, 设为只读 (Android 14 起动态加载的代码文件必须只读) 后用父加载器为启动类加载器的 `DexClassLoader` 加载; 没有带入时退回已安装插件 (与 P7.1 Q187 一致); 两者都没有时给现有的打包应用指引 | P2.2 前 |
| D15 | 扫描: 对项目脚本做静态文本扫描, 命中登记表任一导出 `Entry` 前缀 (按 `.` 边界) 即带入对应插件; 加密脚本与 4 MB 以上的文件不扫 (与 P7.1 `ScriptModuleUsageScanner` 一致). 插件缺失, 未授权, 关闭或版本无导出时打包照常成功, 成功对话框列出未带入的插件与做法 (与 P7.1 Q186 一致). 是否通用于 Pinyin4j 等全部导出插件见附录 D Q2 | P2.1 前 |
| D16 | 打包页 "库" 中新增 `ApkBuildLibrary.JAVA_LIBRARIES` (label `Java Libraries`, 别名 `java-libraries` / `java_libraries` / `javalibraries`), 勾选即带入整个插件, 不按库拆分 | P2.1 前 |
| D17 | 最低宿主: `requiresHostVersion` 填宿主 6.8.1 定版时的 versionCode (宿主 `VERSION_BUILD` 即 versionCode, 6.8.1 发布前尚未确定); 开发期先填当前 6640 并在 P3.3 回填, `check_repository.py` 保证 `app/build.gradle.kts` 与 `.readme/common.json` 两处一致 | P3.3 |
| D18 | 许可证: 插件本身 Apache-2.0; 三个库均为 Apache-2.0, 附带各自的 NOTICE 内容, 写进 `THIRD_PARTY_NOTICES.md` 与启动页 | P0.2 |

## 3. 范围与非目标

范围:

- 插件仓库: 依赖, keep 与导出声明, 信息服务, 启动页, 图标, 测试, CI, 10 种语言 README 与 changelog.
- 宿主: 登记表与已不再内置列表, 打包扫描与带入, 打包应用运行时加载, 指引与 changelog, 设备检查脚本.
- 模板: APK Builder Template 不需要改代码 (打包应用运行时随宿主 6.8.1 的 Runtime Kit 进入模板); 只在设备验证时用本地 Runtime Kit 构建 debug 模板.

非目标:

- 第 2 层 (Rhino 占位对象), `java.lang.Class.forName` 找到插件类 (由调用方加载器查找, 不经 `AndroidClassLoader`).
- 逐库开关, 逐库打包; 升级到新版本的库 (D2).
- D1 未收录的库; 恢复它们另行立项.

## 4. 现状

### 4.1 可直接复用的宿主能力

- `core/plugin/javapackage/PluginJavaPackageCatalog` (登记表: `Entry` 导出包, `Retired` 已不再内置的包), `PluginJavaPackages` (状态缓存, 每个插件版本一个 `PathClassLoader`, 找不到的登记名字记最近 32 个), `PluginJavaPackageMessages` (未安装 / 未授权 / 关闭 / 版本无导出 / 打包应用 / 已不再内置六种消息, 10 种语言). 一个插件可以对应多个 `Entry` (状态按插件包名缓存, 每个 `Entry` 单独判断导出).
- `AndroidClassLoader.loadClass`: 宿主与已加载 dex 都找不到时才查登记表, 不引用登记包的脚本没有开销 (证据第 2 节).
- P7.1 打包带入 Bundled Libraries (宿主 `0d928b9a8`): `RemoteApkBuildRequestPreparer.writeBuildInputsArchive` 把额外文件写进打包应用 `assets/`, `ScriptModuleUsageScanner` 扫描脚本, `PackagedScriptModules.notices` 生成成功对话框提示; 设备检查 `tools/augment/device_packaged_libraries_check.py` (模板插件用同一宿主的 Runtime Kit 构建).
- 设备检查 `tools/augment/device_java_packages_check.py` (`states` / `retired` / `bench`).
- 仓库骨架参照 `AutoJs6-Plugin-Bundled-Libraries` (纯资源插件: 信息服务, 启动页, 发布历史, 宿主外观, Icon Studio, CI 与 `.python` 工具) 与 `D:/idea-projects/AUTOJS6_PLUGIN_NEW_REPO_AGENTS.md`.

### 4.2 缺口

- 新仓库的全部内容.
- 宿主登记表还没有 Java Libraries 的 `Entry`, 三个前缀目前在 `RETIRED` (只给 "已不再内置" 说明).
- 打包流程只认 Bundled Libraries 的脚本模块, 不认 Java 包; 打包应用的 `PluginJavaPackages` 只会找已安装插件.

### 4.3 外部事实

- jar 体积: commons-lang3 3.18.0 688 KB, commons-io 2.8.0 280 KB, jackson-databind 2.13.4.2 约 1.5 MB, jackson-core 2.13.4 约 370 KB, jackson-annotations 2.13.4 约 75 KB. 预计 release APK 2 - 2.5 MB (以 P0.1 实测为准; P0.1 实测约 0.95 MB, 见 P0.1).
- 宿主 6.7.0 的 release 构建未开启 R8 (`isMinifyEnabled` 关), 这些库当年以原名完整存在; 本插件靠 D10 的 keep 规则达到同样效果.
- Apache-2.0 第 4 (d) 条要求随分发附带 NOTICE 内容.

## 5. 目标架构

### 5.1 身份

| 项 | 值 |
| --- | --- |
| 仓库 / 目录 | `AutoJs6-Plugin-Java-Libraries` / `D:/idea-projects/AutoJs6-Plugin-Java-Libraries` |
| `rootProject.name` | `autojs6-plugin-java-libraries` |
| 标题 (不可翻译) | `Java Libraries` |
| applicationId | `io.github.supermonster003.autojs6.plugin.java.libraries` |
| 插件 ID / engine / 变体 | `java-libraries` / `java-libraries` / `default` |
| 信息服务 | `JavaLibrariesInfoService`, action `org.autojs.plugin.INFO`, category `java-libraries` |
| 导出 meta-data | `org.autojs.plugin.javaPackages.EXPORTS` = `org.apache.commons.lang3,org.apache.commons.io,com.fasterxml.jackson` |
| 发布文件名 | `autojs6-plugin-java-libraries-v{VERSION_NAME}-universal-{CRC32}.apk` |
| 起始版本 | `VERSION_NAME` 1.0.0, `VERSION_BUILD` 跟随提交数 |

### 5.2 数据流

```text
脚本 org.apache.commons.lang3.StringUtils.isBlank(s)
  -> Rhino 逐段查类 -> AndroidClassLoader.loadClass: 宿主 / 已加载 dex 都没有
  -> PluginJavaPackageCatalog.itemOf -> Entry(org.apache.commons.lang3 -> Java Libraries)
  -> PluginJavaPackages: 打包应用先找 assets/java-packages/<包名>.zip (D14), 否则已安装插件 APK
       -> 可用: PathClassLoader / DexClassLoader (父 = 启动类加载器) 加载原名类
       -> 不可用: 记下名字, 脚本因错误结束时 PluginJavaPackageMessages 给出安装 / 授权 / 启用 / 更新 / 打包应用指引

打包: 扫描脚本 (D15) 或勾选 Java Libraries (D16)
  -> 从已安装插件 APK 取 dex 与 Java 资源打 zip + json (D13) -> 写进 build inputs archive 的 assets/java-packages/
  -> 模板插件生成打包应用; 成功对话框列出未带入的插件 (D15)
```

---

## 6. 阶段总览

| 阶段 | 版本 | 主题 | 状态 |
| --- | --- | --- | --- |
| P0 | 插件 1.0.0 | 仓库骨架: 依赖与 keep, 身份与信息服务, 启动页与图标, 测试与 CI, README / changelog | 进行中 (jl-p0) |
| P1 | 宿主 6.8.1 | 登记表 `Entry` 与 `RETIRED` 调整, 指引与测试, 设备 `states` 用例扩到 Java Libraries, changelog | 未开始 |
| P2 | 宿主 6.8.1 | 打包带入: 扫描与勾选, 载荷 zip, 成功对话框提示, 打包应用运行时加载, 设备检查 | 未开始 |
| P3 | 插件 1.0.0 | 文档, 索引登记, 最低宿主回填, 发布 (等宿主 6.8.1) | 未开始 |

依赖顺序:

```text
P0 (插件骨架)        P1 (宿主登记表) 与 P0 并行: 不同仓库; P1 的设备用例需要 P0 的插件包
 -> P2 (打包带入)    依赖 P0 (插件 APK 结构) 与 P1 (登记表); 设备检查需要本地 Runtime Kit 构建的模板 debug 包
     -> P3 (发布)    依赖 P0-P2 全部通过与宿主 6.8.1 发布
```

可并行的批次建议:

- 批次 1: 卡片 A = P0 全部 (插件仓库; P0.3 图标视附录 D Q1 结论, 未决时先跳过并登记); 卡片 B = P1.1 + P1.2 (宿主登记表与单元测试, changelog). 两者不同仓库, 可并行. P1.3 设备用例在 P0.1 产出插件包后由卡片 B 补做, 或并入批次 2.
- 批次 2: 卡片 C = P2 全部 (宿主打包与打包应用运行时, 设备检查), 开工前拍板附录 D Q2.
- 批次 3: 卡片 D = P3.1 + P3.2 (文档与索引草稿); P3.3 / P3.4 等宿主 6.8.1 定版.

发布门禁 (P3.4 指向本清单):

1. 插件 `./gradlew assembleDebug assembleRelease test` 通过, 发布校验 (D10 代表类原名) 通过; 宿主涉及模块的单元测试与 `StaticReferenceGuardTest` 通过.
2. 真机: `device_java_packages_check.py` 的 `states` (Java Libraries 版, P1.3) 与 `retired` 通过, P2.4 打包用例通过; 至少一台 API 24 - 28 设备或 AVD 与一台 API 33+ 真机. 无原生库, 不需要 16 KB 设备.
3. 10 种语言资源与文档只用半角标点 (`COMMON_AGENTS.md` 规则), `prepush_check.py` 通过, `THIRD_PARTY_NOTICES.md` 与依赖一致.
4. 发布 APK 为官方签名 release, 内无 `.so`, 体积写进 README; versionCode, 文件名 CRC32 与索引清单一致.
5. changelog 10 种语言同步.

---

## P0: 仓库骨架

状态: 进行中 (子会话 jl-p0). P0.1 (3f30c15), P0.2 (890428d), P0.4 完成.

### P0.1 依赖, keep 与导出声明 (D2, D9, D10, D11)

- [x] (插件) 按 `AUTOJS6_PLUGIN_NEW_REPO_AGENTS.md` 建立 Gradle 工程 (`settings.gradle.kts`, `build-logic`, `gradle/libs.versions.toml`, `version.properties`, `app/`), 填 5.1 节身份; `libs/common-plugin-api.aar` 取自宿主当前产物 (`plugin-api/common-plugin-api` release aar, 与 Bundled Libraries 所带 API 一致).
- [x] (插件) 依赖 D2 三个库 (版本锁死, 依赖树里不得被传递依赖升级), core library desugaring (D11); application meta-data `org.autojs.plugin.javaPackages.EXPORTS` (D9); `proguard-rules.pro` 按 D10. 依赖以 `strictly()` 声明, `verifyExportedLibraryVersions` (挂在 `preBuild`) 核对 debug / release 运行时类路径, 依赖树确认无升级; R8 另需 `-dontwarn` 三个 Android 没有的类 (`java.beans.ConstructorProperties`, `java.beans.Transient`, `java.lang.invoke.MethodHandleProxies`, 与 6.7.0 行为相同).
- [x] (插件) 发布校验: Gradle 任务或 `.python` 脚本读取签名 APK 的 dex, 确认 附录 C代表类以原名存在, 并输出 dex 与 APK 体积; 接入 `appendDigestToReleasedFiles` 之前. 实现为 `verifyReleaseDexClasses` (读 release APK 的 `classes*.dex` 的 class_defs, 拒绝 `lib/` 与 `.so`), 签名门禁 `verifySignedReleaseArtifacts` 依赖它, `appendDigestToReleasedFiles` 依赖签名门禁.
- 验收: `./gradlew assembleDebug assembleRelease` 通过, 发布校验通过; APK 内无 `.so`; 体积写进本条.
  - 结果 (2026-11-05, P0.1 骨架, 尚无启动页与图标): release APK 992,176 字节 (约 0.95 MB, 官方签名, apksigner 校验通过), `classes.dex` 2,234,360 字节 (压缩后 867,236), `classes2.dex` 257,696 字节 (压缩后 104,250, 脱糖库 `j$` 250 个类与 `java.nio.file` 回移类); debug APK 3,112,493 字节. 附录 C 10 个代表类原名都在; 另与 Maven 缓存中的五个 jar 逐类比对, lang3 413 / io 182 / databind 707 / core 127 / annotations 71 个类全部以原名存在于 dex. 比第 4 节估计的 2 - 2.5 MB 小: dex 共享常量池, 比 jar 中逐类的 class 文件紧凑, 再经 zip 压缩.

### P0.2 身份, 信息服务与许可证 (D8, D18)

- [x] (插件) `JavaLibrariesContract` (ID, engine, 指引引用), `JavaLibrariesInfoService` (照 `BundledLibrariesInfoService`), Manifest (`org.autojs.permission.PLUGIN`, 宿主 queries, `WAKE_ACTIVITY`, `requiresHostVersion`), 资源 `plugin_id` / `plugin_engine` / `plugin_variant` / `plugin_requires_host_version`.
- [x] (插件) `LICENSE` (Apache-2.0), `THIRD_PARTY_NOTICES.md` (三个库的许可证与 NOTICE), `.gitattributes`, `.editorconfig`, `AGENTS.md`, `prepush_check.py` 接入.
- 验收: debug 包装到一台设备后插件中心列出 `Java Libraries` 并默认启用 (官方签名或 debug 签名按宿主现行规则).
  - 结果 (2026-11-05): `.python/device_plugin_center_check.py` 在 G8441 (BH900ASK9E, Android 9) 上全部 PASS: 宿主临时换装 6.8.1 debug (versionCode 6640, 测完装回 5935, md5 一致), INFO 服务经包管理器解析到 `JavaLibrariesInfoService`, 签名状态 OFFICIAL, 默认启用策略 true, 打开插件中心后无关闭记录; 截图中 `Java Libraries 1.0.0 (2)` 置顶 (新安装), 开关为开. 注意: 低于 6640 的宿主会把插件标为需要更高版本并写入关闭记录 (宿主现行规则), 设备检查须用 6640 及以上的宿主, 脚本的 `--host-apk` 负责临时换装与还原. `check_repository.py` 与 `prepush_check.py` 已接入, 但要等 P0.5 的 README / changelog 与 P0.3 的图标齐备后才能无错误通过.

### P0.3 图标 (附录 D Q1)

- [ ] (插件) Icon Studio (`D:/webstorm-projects/AutoJs6-Icon-Studio`) 由源图生成全套资源与 `.icons/recipe.json`, 工程测试中的图标断言通过.
- 验收: 启动器与插件中心亮暗主题下图标可辨.

### P0.4 启动页与发布历史 (D12)

- [x] (插件) 启动页 (三个库的版本, 许可证, 导出包, 示例), 发布历史页, 宿主外观跟随; 10 种语言字符串 (`plugin_description` 等), 按 name 排序, 半角标点.
  - 实现: `SettingsActivity` (说明, 库 (单构件显示版本, Jackson 列出三个构件与版本), 在脚本中使用 (附录 C 示例), NOTICE 文件 (与 `THIRD_PARTY_NOTICES.md` 一致, P0.5 测试核对), 发布历史入口); `HostAppearance` / `HostAppearanceActivity` / `AppearanceColors` / `ReleaseHistory` / `ReleaseHistoryActivity` 照 Bundled Libraries, 各节标题用可读化的宿主强调色 (本插件没有开关). 代码与 NOTICE 块等宽, 可选择, 横向滚动, 固定从左到右; 根视图段落方向按页面语言 (阿拉伯语为 RTL, 使拉丁文库名与版本标题也靠右; `TEXT_DIRECTION_LOCALE` 跟随系统语言, 不可用). 12 个字符串 x 10 种语言 (另 values 默认英语).
- 验收: 亮暗主题与阿拉伯语 (RTL) 下页面正常.
  - 结果 (2026-11-05): `.python/device_host_appearance_check.py` (照 Bundled Libraries, 不点按, 改宿主偏好后按字节还原) 在 G8441 (BH900ASK9E, Android 9, 宿主 debug 5935) 上 31 / 34 PASS: 阿拉伯语深色 (操作栏 / 状态栏 / 背景跟随, 标题强调色 #FF4081, 库名靠右, NOTICE 首行从左起), 简中亮色 (浅色强调色加深为 #998568, 库名靠左), 宿主停用时跟随系统亮色; 宿主偏好 md5 还原一致, 宿主重新启用, 插件已卸载. 未过的 3 项是发行历史正文 (`1.0.0`), 资产 `assets/doc/CHANGELOG-*.md` 由 P0.5 生成, 届时复测并写进 P0.5 结果. 截图已人工查看: RTL 下各行靠右, 双向文本次序正确.

### P0.5 测试, CI 与文档

- [ ] (插件) 单元测试: 导出声明与 `libs.versions.toml` 版本一致, 发布历史解析, 外观解析 (照 Bundled Libraries); 仪器测试或 JVM 测试: 用插件 dex 的类跑 附录 C冒烟 (StringUtils, IOUtils, ObjectMapper 读写 JSON).
- [ ] (插件) GitHub Actions (`build.yml`, `markdown.yml`, `icon-studio.yml`, 发布工作流) 照 Bundled Libraries; `.changelog/lang_*.json` 10 种语言与 `generate_markdown.py`, README 10 种语言 (简介, 收录的库, 用法, 打包应用, 体积, 许可证).
- 验收: `./gradlew test` 通过; `generate_markdown.py --check` 与 `check_repository.py` 无错误; 提交前 `VERSION_BUILD` = 提交数 + 1.

---

## P1: 宿主登记表与指引

状态: 未开始. 宿主工作区多会话共用: 只提交自己的文件, 不推送, 不切分支, 构建与其他会话错开.

### P1.1 登记表 (D9)

- [ ] (宿主) `PluginJavaPackageCatalog.ENTRIES` 加三条 `Entry(<前缀>, "io.github.supermonster003.autojs6.plugin.java.libraries", "java-libraries", "Java Libraries", "1.0.0")`; `RETIRED` 删去 `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` 三条; 静态引用白名单说明如需改动一并改.
- [ ] (宿主) `PluginJavaPackageCatalogTest` / `PluginJavaPackageMessagesTest`: 三个前缀命中 `Entry`, 不再命中 `Retired`; 同一插件多前缀时 `unavailableReason` 各自判断导出 (插件只导出其中一个前缀的情况).
- 验收: `:app:testAppDebugUnitTest --tests 'org.autojs.autojs.core.plugin.javapackage.*' --tests '*StaticReferenceGuardTest'` 通过.

### P1.2 changelog

- [ ] (宿主) 6.8.1 `feature` 条目改写为覆盖 Java Libraries (现有 "官方插件导出的 Java 类" 一条补上 Commons Lang3 / IO 与 Jackson), "已不再内置" 一条的举例去掉这三个库; 10 种语言, `generate_markdown.py` 与 `--check`, 翻译复核后 `check_translations.py --record-sync`.
- 验收: `MARKDOWN_OK` 与 `CHANGELOG_TRANSLATIONS_OK`.

### P1.3 设备用例

- [ ] (测试) `device_java_packages_check.py` 加 Java Libraries 的状态用例 (未安装 / 已启用 / 插件中心关闭 / 卸载后各写法, 已启用时冒烟: `StringUtils.isBlank`, `IOUtils.toString`, `ObjectMapper` 序列化与反序列化, 类与加载器来自插件 APK), `retired` 用例中的 Commons 写法换成仍在 `RETIRED` 的库 (如 ICU4J).
- 验收: XQ-AT72 (或当时空闲的 API 31+ 真机) 全部通过; 结果写进宿主证据文档新的一节.

---

## P2: 打包带入

状态: 未开始. 开工前拍板附录 D Q2.

### P2.1 打包流程 (D13, D15, D16)

- [ ] (宿主) `ApkBuildLibrary.JAVA_LIBRARIES` (D16); Java 包扫描器 (D15, 照 `ScriptModuleUsageScanner` 的扫描范围与上限); 载荷生成 (D13): 从已安装插件 APK 读取, 校验插件状态 (授权, 开关, 导出), 写 zip 与 json.
- [ ] (宿主) `RemoteApkBuildRequestPreparer.writeBuildInputsArchive` 写入 `assets/java-packages/`; 成功对话框提示 (插件缺失 / 未授权 / 关闭 / 版本无导出时列出未带入的插件与做法, 10 种语言).
- 验收: 单元测试覆盖扫描 (`.` 边界, 注释与字符串中的名字都算, 加密与超限文件跳过), 载荷内容 (排除项, SHA-256), 勾选与扫描的合并.

### P2.2 打包应用运行时 (D14)

- [ ] (宿主) `PluginJavaPackages` 在打包应用中先读 `assets/java-packages/<包名>.json` 与 zip, 解出, 只读, `DexClassLoader`; SHA-256 不符时重新解出; 没有带入时退回已安装插件; 状态与消息沿用 `PluginJavaPackageState` / `PluginJavaPackageMessages`.
- [ ] (宿主) 静态引用白名单与 `RuntimeResourceScope` 规则检查 (进程级加载器按插件包名一个).
- 验收: 单元测试覆盖解出与校验逻辑; `assembleInrtDebug` 通过.

### P2.3 设备检查

- [ ] (测试) 照 `device_packaged_libraries_check.py` 写打包用例: 本地 `:app:generateRuntimeKit` 构建 Runtime Kit, 模板仓库 `-Pautojs.apkBuilder.templatePlugin.runtimeKitDir` 构建 debug 模板; 应用 a (扫描命中), b (勾选), c (插件未安装时打包); 安装后在插件未安装 / 已安装时运行, 核对冒烟结果与缺失时的消息.
- 验收: 一台真机全部通过, 结果写进宿主证据文档; 装回原宿主, 测试包与模板并核对 md5.

---

## P3: 文档与发布

状态: 未开始. P3.3 / P3.4 等宿主 6.8.1 定版 (D7, D17).

### P3.1 文档

- [ ] (文档) 文档站与 Offline Docs 中 Java 交互相关页加一节: 官方插件导出的 Java 包 (Pinyin4j, Java Libraries), 已不再内置的库与替代, 打包应用的带入方式.

### P3.2 索引草稿

- [ ] (索引) 准备 AutoJs6-Official-Plugins-Index 登记所需的元数据 (按索引生成器的 flavor 要求), 推送后再生成.

### P3.3 最低宿主回填 (D17)

- [ ] (插件) 宿主 6.8.1 定版后把 `requiresHostVersion` 改为其 versionCode, `.readme/common.json` 同步.

### P3.4 发布

- [ ] (发布) 维护者在 GitHub 建仓后推送; 按第 6 节发布门禁发布 1.0.0, 登记索引.

---

## 附录 C: 代表类清单 (D10 发布校验与冒烟)

- `org.apache.commons.lang3.StringUtils`, `org.apache.commons.lang3.time.DateFormatUtils`, `org.apache.commons.lang3.builder.ToStringBuilder`
- `org.apache.commons.io.IOUtils`, `org.apache.commons.io.FileUtils`, `org.apache.commons.io.FilenameUtils`
- `com.fasterxml.jackson.databind.ObjectMapper`, `com.fasterxml.jackson.databind.JsonNode`, `com.fasterxml.jackson.core.JsonFactory`, `com.fasterxml.jackson.annotation.JsonProperty`

---

## 附录 D: 待决事项

格式: 编号, 问题, 选项 (推荐项标 "(推荐)", 已先行的标 "(已先行)"), 影响, 改选返工. 拍板后在此改写结论与日期, 同步看板.

- Q1 图标源图 | (a) 维护者提供亮 / 暗两张源图, 子会话用 Icon Studio 生成 (推荐, 与 3-Tally Sheet 等一致) / (b) 子会话以文字标志派生一版, 维护者验收 | 影响: P0.3 | 改选返工: 小 | 状态: 已拍板 (2026-11-05, 维护者选: 由子会话设计并用 Icon Studio 生成, 维护者验收)
- Q2 打包扫描的范围 | (a) 通用于登记表中全部导出插件 (Pinyin4j 1.4.0 也随之可带入; 载荷含 Java 资源, pinyin4j 的拼音数据在其中) (推荐) / (b) 只做 Java Libraries | 影响: P2.1, P2.3 | 改选返工: 小 (过滤条件一处, 用例少一组) | 状态: 已拍板 (2026-11-05, 维护者选 (a) 通用于登记表全部导出插件)
