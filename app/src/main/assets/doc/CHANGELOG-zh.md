******

### 发行历史

******

# v1.0.0

###### 2026/11/05

* `提示` 需要 AutoJs6 6.8.1 (构建 6641) 或更高版本
* `提示` AutoJs6 6.8.0 起不再内置 Apache Commons Lang3, Commons IO 与 Jackson; 安装并启用本插件后, 脚本照常以全名使用这些库的类, 写法与 AutoJs6 6.7.0 相同, 原有脚本无需修改
* `新增` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 与 Jackson 2.13.4.2 (core 与 annotations 为 2.13.4), 版本与 AutoJs6 6.7.0 内置的相同
* `新增` 插件只是一个资源包: AutoJs6 直接从插件加载这些类, 脚本运行时不启动插件进程, 插件不申请任何权限
* `新增` 用 AutoJs6 打包应用时可以带入这些类 (自动扫描脚本, 也可以在库列表中勾选), 打包后的应用在没有本插件的设备上也能使用
* `新增` 插件页面显示各库的版本, 许可证, Java 包, 使用示例, NOTICE 文件与发行历史
* `新增` 插件页面跟随 AutoJs6 的语言, 夜间模式与主题色, 未安装 AutoJs6 或无法读取其设置时跟随系统
* `依赖` 附加 AutoJs6 Common Plugin API
* `依赖` 附加 desugar_jdk_libs_nio 2.1.5
