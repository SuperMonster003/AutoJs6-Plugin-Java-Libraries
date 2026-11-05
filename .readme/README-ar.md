<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>مكتبات Java من Apache Commons Lang3 وCommons IO وJackson للاستخدام المباشر في سكربتات AutoJs6</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Java-Libraries?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=534BAE&label=License"/></a>
  </p>
</div>

******

### اللغات (Languages)

******

يدعم README.md الحالي اللغات التالية:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ru.md)
- العربية [ar] # الحالية

******

### مقدمة

******

يعيد Java Libraries إلى الجهاز مكتبات Java الثلاث التي كانت مدمجة في AutoJs6 سابقا: Apache Commons Lang3 وApache Commons IO وJackson. بدءا من AutoJs6 6.8.0 لم تعد حزمة AutoJs6 تتضمنها; وبعد تثبيت هذا المكون وتفعيله في AutoJs6 6.8.1 أو أحدث تستخدم السكربتات أصنافا مثل `org.apache.commons.lang3.StringUtils` و`org.apache.commons.io.IOUtils` و`com.fasterxml.jackson.databind.ObjectMapper` بأسمائها الكاملة كما في AutoJs6 6.7.0 والإصدارات الأقدم تماما.

المكون مجرد حزمة موارد: يحمل AutoJs6 الأصناف مباشرة من حزمة المكون, ولا تشغل السكربتات عملية المكون أبدا, ولا يطلب المكون أي إذن. السكربتات التي لا تشير إلى هذه الأصناف لا تتأثر, والأصناف التي لا يستخدمها أحد لا تحمل أبدا, وإنما تشغل مساحة في الحزمة فقط.

******

### المكتبات المضمنة

******

توجد أصناف كل مكتبة في حزمة Java المذكورة في الجدول أدناه, بالإصدارات التي كان AutoJs6 6.7.0 يتضمنها. تراخيص المكتبات وملفات NOTICE الخاصة بها في [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md), وتعرضها صفحة المكون أيضا.

| المكتبة | عناصر Maven وإصداراتها | حزمة Java | الترخيص |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### الاستخدام

******

1. استخدم AutoJs6 6.8.1 (البناء 6640) أو أحدث.
2. ثبت Java Libraries من مركز الاضافات في AutoJs6 (أو نزل ملف APK من [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases)) وتأكد من أن المكون مفعل.
3. استخدم أصناف المكتبات بأسمائها الكاملة في السكربتات (أو بعد `importClass` أو `importPackage`), مثلا:

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

> توجد حزمة عامة واحدة فقط بلا مكتبات أصلية, للأجهزة التي تعمل بنظام Android 7.0 (API 24) أو أحدث.

******

### التطبيقات المحزمة

******

عندما يحزم AutoJs6 السكربتات في تطبيق مستقل يمكنه أن يأخذ أصناف هذا المكون معه, فيستطيع التطبيق المحزم استخدام المكتبات على الأجهزة التي لا يوجد فيها المكون:

- أثناء التحزيم يفحص AutoJs6 سكربتات المشروع ويأخذ الأصناف معه حالما يجد اسما يبدأ بـ `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` (لا تفحص السكربتات المشفرة ولا الملفات التي يزيد حجمها على 4 MB).
- عندما يركب الاسم من سلاسل نصية ولا يجده الفحص, حدد Java Libraries في قائمة المكتبات في صفحة التحزيم.
- يتطلب أخذ الأصناف أن يكون المكون مثبتا ومفعلا على الجهاز الذي يحزم التطبيق; وإن تعذر ذلك يكتمل التحزيم مع ذلك ويعرض مربع الحوار النهائي المكونات التي لم تؤخذ وما يجب فعله.
- يستخدم التطبيق المحزم النسخة التي يحملها أولا, والمكون المثبت على الجهاز إن لم يحمل نسخة.

******

### عندما يكون المكون غير متاح

******

عندما يكون المكون غير مثبت أو غير مصرح له أو معطلا في مركز الاضافات أو قديما جدا, لا تستطيع السكربتات استخدام هذه الأصناف:

- ذكر اسم الصنف وحده ليس خطأ (يعامل Rhino الاسم الذي لا يجده كحزمة Java); يظهر الخطأ عندما يستدعي السكربت دالة أو ينشئ كائنا أو يمرر الاسم إلى `importClass`.
- عندما ينتهي السكربت بخطأ كهذا يعرض AutoJs6 في وحدة التحكم بعد الخطأ إرشادا يوضح هل يجب تثبيت المكون أو التصريح له أو تفعيله أو تحديثه; أما الأخطاء التي يلتقطها السكربت بنفسه فلا ترفق بإرشاد.
- عندما لا يحمل التطبيق المحزم نسخة ولا يوجد على جهازه مكون صالح أيضا, يطلب الإرشاد تثبيت المكون وتفعيله على الجهاز.

******

### التوافق

******

- يتطلب AutoJs6 6.8.1 (البناء 6640) أو أحدث. يتضمن AutoJs6 6.7.0 والإصدارات الأقدم هذه المكتبات بنفسه ولا يحتاج إلى المكون; ولا يستطيع AutoJs6 6.8.0 استخدام المكون.
- يدعم Android 7.0 (API 24) أو أحدث, بحزمة واحدة لجميع معماريات المعالجات.
- إصدارات المكتبات هي التي كان AutoJs6 6.7.0 يتضمنها, لذا لا تحتاج السكربتات الحالية إلى أي تعديل.
- كما في AutoJs6 6.7.0 تمر المكتبات عبر core library desugaring: تستخدم الدوال ذات المعاملات أو النتائج من أنواع `java.time` الأنواع المحولة `j$.time`.
- لا يجد `java.lang.Class.forName` أصناف المكون بالاسم; أشر إليها بأسمائها الكاملة أو استخدم `importClass`.

******

### حجم الحزمة

******

حجم حزمة release نحو 1.2 MB. يبلغ مجموع ملفات jar للمكتبات الثلاث نحو 2.9 MB; تحتفظ الحزمة بكل صنف باسمه الأصلي, ويجعلها dex بمجمع ثوابته المشترك والضغط أصغر.

******

### الأسئلة الشائعة

******

#### لماذا لم يعد AutoJs6 يتضمن هذه المكتبات?

بعض السكربتات فقط تستخدمها. نقلها إلى مكون يجعل حزمة AutoJs6 أصغر, ومن يحتاج إليها يثبت المكون. المكتبات في المكون هي الإصدارات التي كان AutoJs6 6.7.0 يتضمنها.

#### لماذا لا تحدث المكتبات إلى إصدارات أحدث?

الإصدارات الأحدث لم تعد تدعم Android 7.x: يتطلب Jackson نظام Android 8.0 بدءا من 2.14, وتفشل إصدارات Commons IO الأحدث على Android 7.x. ولكي يعمل المكون على جميع الأجهزة التي يدعمها AutoJs6 ويبقى كما هو للسكربتات الحالية, يحتفظ بإصدارات AutoJs6 6.7.0.

#### هل يتصل المكون بالإنترنت أو يعمل في الخلفية?

لا. لا يطلب المكون أي إذن, وتشغيل السكربت لا يشغل عملية المكون أبدا; تعمل الأصناف داخل AutoJs6 الذي يشغل السكربت.

#### يقول الخطأ إن المكون يحتاج إلى تصريح. ماذا أفعل?

الإصدار المنشور المثبت من مركز الاضافات في AutoJs6 أو من Releases لهذا المشروع لا يحتاج إلى تصريح. أما النسخة التي تبنيها بنفسك فلها توقيع مختلف ويجب التصريح لها أولا في مركز الاضافات حسب التعليمات.

#### هل ستنضم إلى هذا المكون مكتبات أخرى لم يعد AutoJs6 يتضمنها (مثل ICU4J)?

ليس حاليا. ICU4J كبيرة جدا ويمكن نقل معظم استخداماتها إلى `android.icu` في النظام, وLog4j 1.x لم تعد تصان. عندما يستخدم سكربت هذه المكتبات يوضح AutoJs6 أنها لم تعد مضمنة.

******

### واجهة المكون الإضافي

******

المعلومات التالية موجهة لمطوري مضيف AutoJs6 والمكونات; يستخدم المضيف هذه المعرفات لاكتشاف المكون وتحميل أصنافه:

```text
application id: io.github.supermonster003.autojs6.plugin.java.libraries
plugin id: java-libraries
engine: java-libraries
info service: org.autojs.plugin.INFO (category java-libraries)
exported packages: meta-data org.autojs.plugin.javaPackages.EXPORTS = org.apache.commons.lang3,org.apache.commons.io,com.fasterxml.jackson
wake action: org.autojs.plugin.action.WAKE
minimum host build: 6640
native library: none (all ABIs)
```

******

### خارطة الطريق

******

قائمة التحقق قبل الإصدار والقرارات وخطوات الإصدار مسجلة في ROADMAP.md; ويحمل المضيف الأصناف عبر الحزمة `org.autojs.autojs.core.plugin.javapackage` في مستودع AutoJs6.

- [عرض ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### سجل الإصدارات

******

#### v1.0.0

_2026/11/05_

- `تلميح` يتطلب AutoJs6 6.8.1 (البناء 6640) أو أحدث
- `تلميح` بدءا من AutoJs6 6.8.0 لم تعد Apache Commons Lang3 وCommons IO وJackson مدمجة; ومع تثبيت هذا المكون وتفعيله تستخدم السكربتات أصناف هذه المكتبات بأسمائها الكاملة كما في AutoJs6 6.7.0, ولا تحتاج السكربتات الحالية إلى أي تعديل
- `ميزة` Apache Commons Lang3 3.18.0 وApache Commons IO 2.8.0 وJackson 2.13.4.2 (core وannotations بالإصدار 2.13.4), وهي الإصدارات التي كان AutoJs6 6.7.0 يتضمنها
- `ميزة` المكون مجرد حزمة موارد: يحمل AutoJs6 الأصناف مباشرة من المكون, وتشغيل السكربتات لا يشغل عملية المكون أبدا, ولا يطلب المكون أي إذن
- `ميزة` يمكن للتطبيقات المحزمة بـ AutoJs6 أن تحمل هذه الأصناف (يعثر عليها بفحص السكربتات أو تحدد في قائمة المكتبات) ثم تستخدمها على الأجهزة التي لا يوجد فيها المكون
- `ميزة` تعرض صفحة المكون إصدار كل مكتبة وترخيصها وحزمة Java الخاصة بها ومثالا وملفات NOTICE وسجل الإصدارات
- `ميزة` تتبع صفحة المكون لغة AutoJs6 ووضعه الليلي ولون سمته, وتتبع النظام عندما لا يكون AutoJs6 مثبتا أو تتعذر قراءة إعداداته
- `تبعية` إرفاق AutoJs6 Common Plugin API
- `تبعية` إرفاق desugar_jdk_libs_nio 2.1.5

##### لمزيد من سجل الإصدارات

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-ar.md)

******

### البناء

******

هذا القسم موجه للمطورين الراغبين في بناء المكون من المصدر.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### الترخيص

******

شيفرة المكون مرخصة بموجب [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE). والمكتبات الثلاث مرخصة كذلك بموجب Apache License 2.0, وملفات NOTICE الخاصة بها في [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md); وتتضمن الحزمة أيضا بيئة تشغيل core library desugaring من Android (GNU GPL v2 مع Classpath Exception).

******

### روابط

******

- مشروع AutoJs6: https://github.com/SuperMonster003/AutoJs6
- إشعارات الأطراف الخارجية: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
