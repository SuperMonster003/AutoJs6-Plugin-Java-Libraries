<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>Apache Commons Lang3, Commons IO и Jackson для прямого использования Java-классов в скриптах AutoJs6</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Java-Libraries?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Языки (Languages)

******

Текущий README.md поддерживает следующие языки:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ko.md)
- Русский [ru] # текущий
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ar.md)

******

### Введение

******

Java Libraries возвращает на устройство три Java-библиотеки, которые раньше входили в AutoJs6: Apache Commons Lang3, Apache Commons IO и Jackson. Начиная с AutoJs6 6.8.0 пакет AutoJs6 их больше не содержит; если этот плагин установлен и включён в AutoJs6 6.8.1 или новее, скрипты используют классы вроде `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils` и `com.fasterxml.jackson.databind.ObjectMapper` по полным именам, точно так же, как в AutoJs6 6.7.0 и более ранних версиях.

Плагин - это лишь пакет ресурсов: AutoJs6 загружает классы прямо из пакета плагина, скрипты никогда не запускают процесс плагина, а сам плагин не запрашивает никаких разрешений. Скрипты, которые не обращаются к этим классам, не затронуты, а неиспользуемые классы никогда не загружаются и лишь занимают место в пакете.

******

### Включённые библиотеки

******

Классы каждой библиотеки находятся в Java-пакете из таблицы ниже, в тех версиях, что входили в AutoJs6 6.7.0. Лицензии и файлы NOTICE библиотек приведены в [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md), их также показывает страница плагина.

| Библиотека | Артефакты Maven и версии | Java-пакет | Лицензия |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### Использование

******

1. Используйте AutoJs6 6.8.1 (сборка 6640) или новее.
2. Установите Java Libraries из Центра плагинов AutoJs6 (или скачайте APK из [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases)) и убедитесь, что плагин включён.
3. Используйте классы библиотек в скриптах по полным именам (или после `importClass` или `importPackage`), например:

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

> Есть только один универсальный пакет без нативных библиотек для устройств с Android 7.0 (API 24) или новее.

******

### Упакованные приложения

******

Когда AutoJs6 упаковывает скрипты в отдельное приложение, он может взять с собой классы этого плагина, и упакованное приложение сможет использовать библиотеки на устройствах без плагина:

- При упаковке AutoJs6 просматривает скрипты проекта и берёт классы с собой, как только находит имя, начинающееся с `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` (зашифрованные скрипты и файлы больше 4 МБ не просматриваются).
- Если имя собирается из строк и просмотр его не находит, отметьте Java Libraries в списке библиотек на странице упаковки.
- Чтобы взять классы с собой, плагин должен быть установлен и включён на устройстве, которое упаковывает приложение; если это невозможно, упаковка всё равно завершается, а итоговый диалог перечисляет пропущенные плагины и что делать.
- Упакованное приложение сначала использует взятую с собой копию, а если её нет - плагин, установленный на устройстве.

******

### Когда плагин недоступен

******

Если плагин не установлен, не авторизован, отключён в Центре плагинов или слишком старый, скрипты не могут использовать эти классы:

- Само упоминание класса не вызывает ошибку (Rhino считает ненайденное имя Java-пакетом); ошибка возникает, когда скрипт вызывает метод, создаёт объект или передаёт имя в `importClass`.
- Когда скрипт завершается такой ошибкой, AutoJs6 выводит в консоль после ошибки подсказку: установить, авторизовать, включить или обновить плагин; ошибки, которые скрипт перехватывает сам, подсказок не получают.
- Если упакованное приложение не несёт копии, а на его устройстве тоже нет пригодного плагина, подсказка предлагает установить и включить плагин на устройстве.

******

### Совместимость

******

- Требуется AutoJs6 6.8.1 (сборка 6640) или новее. AutoJs6 6.7.0 и более ранние версии сами содержат эти библиотеки и не нуждаются в плагине; AutoJs6 6.8.0 не может использовать плагин.
- Поддерживается Android 7.0 (API 24) или новее, один пакет для всех архитектур процессора.
- Версии библиотек те же, что входили в AutoJs6 6.7.0, поэтому существующие скрипты менять не нужно.
- Как и в AutoJs6 6.7.0, библиотеки проходят core library desugaring: методы с параметрами или результатами типов `java.time` используют преобразованные типы `j$.time`.
- `java.lang.Class.forName` не находит классы плагина по имени; обращайтесь к ним по полным именам или используйте `importClass`.

******

### Размер Пакета

******

Пакет release занимает около 1.2 МБ. Jar-файлы трёх библиотек в сумме занимают около 2,9 МБ; пакет сохраняет каждый класс под исходным именем, а dex с общим пулом констант и сжатие делают его меньше.

******

### Частые Вопросы

******

#### Почему AutoJs6 больше не содержит эти библиотеки?

Их используют лишь некоторые скрипты. В виде плагина они уменьшают пакет AutoJs6, а кому они нужны, тот устанавливает плагин. Библиотеки в плагине - те же версии, что входили в AutoJs6 6.7.0.

#### Почему не обновить библиотеки до новых версий?

Новые версии больше не поддерживают Android 7.x: Jackson начиная с 2.14 требует Android 8.0, а более новые версии Commons IO дают ошибки на Android 7.x. Чтобы работать на всех устройствах, которые поддерживает AutoJs6, и не меняться для существующих скриптов, плагин сохраняет версии AutoJs6 6.7.0.

#### Плагин выходит в сеть или работает в фоне?

Нет. Плагин не запрашивает никаких разрешений, а запуск скрипта никогда не запускает процесс плагина; классы выполняются в AutoJs6, который выполняет скрипт.

#### Ошибка сообщает, что плагин нужно авторизовать. Что делать?

Выпущенная версия, установленная из Центра плагинов AutoJs6 или из Releases этого проекта, авторизации не требует. Собственная сборка имеет другую подпись, и её сначала нужно авторизовать в Центре плагинов по подсказке.

#### Войдут ли в плагин другие библиотеки, которых больше нет в AutoJs6 (например, ICU4J)?

Пока нет. ICU4J очень велика, и большинство её применений можно перевести на системный `android.icu`, а Log4j 1.x больше не поддерживается. Когда скрипт использует эти библиотеки, AutoJs6 объясняет, что они больше не входят в его состав.

******

### Интерфейс Плагина

******

Следующие сведения предназначены разработчикам хоста AutoJs6 и плагинов; по этим идентификаторам хост находит плагин и загружает его классы:

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

### Дорожная Карта

******

Контрольный список перед выпуском, решения и шаги выпуска ведутся в ROADMAP.md; хост загружает классы с помощью пакета `org.autojs.autojs.core.plugin.javapackage` репозитория AutoJs6.

- [Открыть ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### История Выпусков

******

#### v1.0.0

_2026/11/05_

- `Подсказка` Требуется AutoJs6 6.8.1 (сборка 6640) или новее
- `Подсказка` Начиная с AutoJs6 6.8.0 Apache Commons Lang3, Commons IO и Jackson больше не встроены; с установленным и включённым плагином скрипты используют классы этих библиотек по полным именам, как в AutoJs6 6.7.0, и существующие скрипты менять не нужно
- `Функция` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 и Jackson 2.13.4.2 (core и annotations 2.13.4) - те же версии, что входили в AutoJs6 6.7.0
- `Функция` Плагин - лишь пакет ресурсов: AutoJs6 загружает классы прямо из плагина, запуск скриптов никогда не запускает процесс плагина, а плагин не запрашивает никаких разрешений
- `Функция` Приложения, упакованные в AutoJs6, могут взять эти классы с собой (найденные просмотром скриптов или отмеченные в списке библиотек) и затем использовать их на устройствах без плагина
- `Функция` Страница плагина показывает версию, лицензию и Java-пакет каждой библиотеки, пример, файлы NOTICE и историю выпусков
- `Функция` Страница плагина следует языку, ночному режиму и цвету темы AutoJs6, а если AutoJs6 не установлен или его настройки не читаются - системе
- `Зависимость` Добавлен AutoJs6 Common Plugin API
- `Зависимость` Добавлен desugar_jdk_libs_nio 2.1.5

##### Больше истории выпусков

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-ru.md)

******

### Сборка

******

Этот раздел предназначен разработчикам, желающим собрать плагин из исходников.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### Лицензия

******

Код плагина распространяется по [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE). Три библиотеки также используют Apache License 2.0, их файлы NOTICE приведены в [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md); пакет также содержит среду выполнения core library desugaring от Android (GNU GPL v2 с Classpath Exception).

******

### Ссылки

******

- Проект AutoJs6: https://github.com/SuperMonster003/AutoJs6
- Сторонние уведомления: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
