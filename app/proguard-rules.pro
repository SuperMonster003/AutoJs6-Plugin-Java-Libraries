# The AIDL interfaces of the plugin API are called by AutoJs6 under their original names.
# zh-CN: 插件 API 的 AIDL 接口由 AutoJs6 按原名调用.
-dontwarn kotlinx.parcelize.Parcelize
-keep class org.autojs.plugin.** { *; }

# ROADMAP.md D10: AutoJs6 scripts reach these packages by name through the plugin APK (D9), so every class and member
# stays whole and unrenamed, as in the AutoJs6 6.7.0 builds that included the libraries without R8.
# zh-CN: ROADMAP.md D10: AutoJs6 脚本经插件 APK 按名称访问这些包 (D9), 因此全部类与成员完整保留且不改名, 与内置这些库且未开 R8
# 的 AutoJs6 6.7.0 构建相同.
-keep class org.apache.commons.lang3.** { *; }
-keep class org.apache.commons.io.** { *; }
-keep class com.fasterxml.jackson.** { *; }

# Jackson reads annotations and generic signatures by reflection.
# zh-CN: Jackson 通过反射读取注解与泛型签名.
-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisible*Annotations, AnnotationDefault

# Classes the libraries refer to that Android does not have. Jackson catches the missing java.beans classes
# (Java7SupportImpl); MethodInvokers.asInterfaceInstance of Commons Lang fails as it did in AutoJs6 6.7.0.
# zh-CN: 这些库引用而 Android 没有的类. Jackson 会捕获 java.beans 类缺失 (Java7SupportImpl); Commons Lang 的
# MethodInvokers.asInterfaceInstance 与 AutoJs6 6.7.0 中一样无法使用.
-dontwarn java.beans.ConstructorProperties
-dontwarn java.beans.Transient
-dontwarn java.lang.invoke.MethodHandleProxies
