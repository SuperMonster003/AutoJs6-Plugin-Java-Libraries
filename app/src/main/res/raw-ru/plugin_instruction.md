Java Libraries возвращает на устройство три Java-библиотеки, которые раньше входили в AutoJs6: Apache Commons Lang3, Apache Commons IO и Jackson. Начиная с AutoJs6 6.8.0 пакет AutoJs6 их больше не содержит; если этот плагин установлен и включён в AutoJs6 6.8.1 или новее, скрипты используют классы вроде `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils` и `com.fasterxml.jackson.databind.ObjectMapper` по полным именам, точно так же, как в AutoJs6 6.7.0 и более ранних версиях.

Плагин - это лишь пакет ресурсов: AutoJs6 загружает классы прямо из пакета плагина, скрипты никогда не запускают процесс плагина, а сам плагин не запрашивает никаких разрешений. Скрипты, которые не обращаются к этим классам, не затронуты, а неиспользуемые классы никогда не загружаются и лишь занимают место в пакете.

| Библиотека | Артефакты Maven и версии | Java-пакет | Лицензия |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

### Использование

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

### Когда плагин недоступен

Если плагин не установлен, не авторизован, отключён в Центре плагинов или слишком старый, скрипты не могут использовать эти классы:

- Само упоминание класса не вызывает ошибку (Rhino считает ненайденное имя Java-пакетом); ошибка возникает, когда скрипт вызывает метод, создаёт объект или передаёт имя в `importClass`.
- Когда скрипт завершается такой ошибкой, AutoJs6 выводит в консоль после ошибки подсказку: установить, авторизовать, включить или обновить плагин; ошибки, которые скрипт перехватывает сам, подсказок не получают.
- Если упакованное приложение не несёт копии, а на его устройстве тоже нет пригодного плагина, подсказка предлагает установить и включить плагин на устройстве.

Исходный код, история выпусков и сторонние лицензии - на [странице проекта](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
