يعيد Java Libraries إلى الجهاز مكتبات Java الثلاث التي كانت مدمجة في AutoJs6 سابقا: Apache Commons Lang3 وApache Commons IO وJackson. بدءا من AutoJs6 6.8.0 لم تعد حزمة AutoJs6 تتضمنها; وبعد تثبيت هذا المكون وتفعيله في AutoJs6 6.8.1 أو أحدث تستخدم السكربتات أصنافا مثل `org.apache.commons.lang3.StringUtils` و`org.apache.commons.io.IOUtils` و`com.fasterxml.jackson.databind.ObjectMapper` بأسمائها الكاملة كما في AutoJs6 6.7.0 والإصدارات الأقدم تماما.

المكون مجرد حزمة موارد: يحمل AutoJs6 الأصناف مباشرة من حزمة المكون, ولا تشغل السكربتات عملية المكون أبدا, ولا يطلب المكون أي إذن. السكربتات التي لا تشير إلى هذه الأصناف لا تتأثر, والأصناف التي لا يستخدمها أحد لا تحمل أبدا, وإنما تشغل مساحة في الحزمة فقط.

| المكتبة | عناصر Maven وإصداراتها | حزمة Java | الترخيص |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

### الاستخدام

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

### عندما يكون المكون غير متاح

عندما يكون المكون غير مثبت أو غير مصرح له أو معطلا في مركز الاضافات أو قديما جدا, لا تستطيع السكربتات استخدام هذه الأصناف:

- ذكر اسم الصنف وحده ليس خطأ (يعامل Rhino الاسم الذي لا يجده كحزمة Java); يظهر الخطأ عندما يستدعي السكربت دالة أو ينشئ كائنا أو يمرر الاسم إلى `importClass`.
- عندما ينتهي السكربت بخطأ كهذا يعرض AutoJs6 في وحدة التحكم بعد الخطأ إرشادا يوضح هل يجب تثبيت المكون أو التصريح له أو تفعيله أو تحديثه; أما الأخطاء التي يلتقطها السكربت بنفسه فلا ترفق بإرشاد.
- عندما لا يحمل التطبيق المحزم نسخة ولا يوجد على جهازه مكون صالح أيضا, يطلب الإرشاد تثبيت المكون وتفعيله على الجهاز.

الشيفرة المصدرية وسجل الإصدارات وتراخيص الأطراف الخارجية في [صفحة المشروع](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
