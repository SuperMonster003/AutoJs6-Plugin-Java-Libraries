يوفر Java Libraries لسكربتات AutoJs6 ثلاث مكتبات Java: Apache Commons Lang3 و Apache Commons IO و Jackson. بعد تفعيله تواصل السكربتات استخدام أصنافها بأسمائها الكاملة, كما في AutoJs6 6.7.0 والإصدارات الأقدم.

### البدء

1. تأكد من أن هذا المكون مفعل في مركز الاضافات في AutoJs6.
2. استخدم في السكربت أصناف المكتبات بأسمائها الكاملة, مثل `org.apache.commons.io.IOUtils`; انظر المثال أدناه.
3. تعرض صفحة المكون إصدار كل مكتبة وعناصر Maven وحزم Java الخاصة بها, وتفتح من مركز الاضافات أو من أيقونة Java Libraries على الشاشة الرئيسية.

### الميزات الرئيسية

- **المكتبات**: تعرض صفحة المكون لكل من Apache Commons Lang3 و Apache Commons IO و Jackson الإصدار وعناصر Maven وحزم Java والترخيص.
- **الاستخدام في السكربتات**: تقدم صفحة المكون مثالا يمكن تشغيله كما هو, وتوضح التعليقات ناتج كل سطر.
- **ملفات NOTICE**: تعرض صفحة المكون ملفات NOTICE كما تنشرها المكتبات.

### الاستخدام في السكربتات

استخدم الأصناف بأسمائها الكاملة; وتوضح التعليقات ناتج كل سطر:

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

- يمكنك أيضا استدعاء `importClass(org.apache.commons.lang3.StringUtils)` أولا ثم كتابة `StringUtils` فقط.
- عندما يكون المكون غير متاح فإن ذكر اسم الصنف وحده ليس خطأ (يعامل Rhino الاسم الذي لا يجده كحزمة Java); يظهر الخطأ عندما يستدعي السكربت دالة أو ينشئ كائنا أو يمرر الاسم إلى `importClass`.

### المتطلبات

- Android 7.0 أو أحدث, و AutoJs6 6.8.1 أو أحدث.
- هذا المكون مجرد حزمة موارد: لا تشغل السكربتات عمليته ولا يحتاج إلى أي إذن; والسكربتات التي لا تشير إلى هذه الأصناف لا تتأثر.

### الأسئلة الشائعة

**السكربت لا يستطيع استخدام هذه الأصناف**

تأكد من أن هذا المكون مثبت ومفعل في مركز الاضافات, وأن إصدار AutoJs6 هو 6.8.1 أو أحدث. عندما ينتهي السكربت بخطأ كهذا يوضح AutoJs6 في وحدة التحكم بعد الخطأ هل يجب تثبيت المكون أو التصريح له أو تفعيله أو تحديثه; أما الأخطاء التي يلتقطها السكربت بنفسه باستخدام `catch` فلا ترفق بهذا الإرشاد.

**الاستخدام في التطبيقات المحزمة**

عند التحزيم يأخذ AutoJs6 أصناف هذا المكون معه إذا احتوت السكربتات على اسم يبدأ بـ `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson`; وإذا لم يجد الفحص مثل هذا الاسم فحدد Java Libraries في قائمة المكتبات في صفحة التحزيم. التطبيق الذي يحملها يستطيع استخدام هذه الأصناف على الأجهزة التي لا تحتوي على هذا المكون, وإلا فإنه يستخدم المكون المثبت على الجهاز.

الشيفرة المصدرية وتراخيص الأطراف الخارجية في [صفحة المشروع](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
