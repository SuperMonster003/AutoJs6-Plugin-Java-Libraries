<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>Apache Commons Lang3, Commons IO y Jackson para usar directamente como clases Java en scripts de AutoJs6</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Java-Libraries?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Idiomas (Languages)

******

El README.md actual admite los siguientes idiomas:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-fr.md)
- Español [es] # actual
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ar.md)

******

### Introducción

******

Java Libraries devuelve al dispositivo las tres bibliotecas Java que AutoJs6 incluía antes: Apache Commons Lang3, Apache Commons IO y Jackson. Desde AutoJs6 6.8.0, el paquete de AutoJs6 ya no las incluye; con este plugin instalado y activado en AutoJs6 6.8.1 o posterior, los scripts usan por su nombre completo clases como `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils` o `com.fasterxml.jackson.databind.ObjectMapper`, igual que con AutoJs6 6.7.0 y versiones anteriores.

El plugin es solo un paquete de recursos: AutoJs6 carga las clases directamente del paquete del plugin, los scripts nunca inician el proceso del plugin y el plugin no pide ningún permiso. Los scripts que no usan estas clases no se ven afectados, y las clases que nadie usa nunca se cargan; solo ocupan espacio en el paquete.

******

### Bibliotecas incluidas

******

Las clases de cada biblioteca están en el paquete Java de la tabla siguiente, en las versiones que incluía AutoJs6 6.7.0. Las licencias y los archivos NOTICE de las bibliotecas están en [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md), y la página del plugin también los muestra.

| Biblioteca | Artefactos Maven y versiones | Paquete Java | Licencia |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### Uso

******

1. Use AutoJs6 6.8.1 (build 6640) o posterior.
2. Instale Java Libraries desde el Centro de plugins de AutoJs6 (o descargue el APK desde [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases)) y compruebe que el plugin está activado.
3. Use las clases de las bibliotecas por su nombre completo en los scripts (o tras `importClass` o `importPackage`), por ejemplo:

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

> Hay un único paquete universal, sin bibliotecas nativas, para dispositivos con Android 7.0 (API 24) o posterior.

******

### Aplicaciones empaquetadas

******

Cuando AutoJs6 empaqueta scripts en una aplicación independiente, puede llevar consigo las clases de este plugin, de modo que la aplicación empaquetada pueda usar las bibliotecas en dispositivos sin el plugin:

- Al empaquetar, AutoJs6 analiza los scripts del proyecto y lleva consigo las clases en cuanto encuentra un nombre que empieza por `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` (los scripts cifrados y los archivos de más de 4 MB no se analizan).
- Cuando un nombre se forma uniendo cadenas y el análisis no lo encuentra, marque Java Libraries en la lista de bibliotecas de la página de empaquetado.
- Llevar las clases requiere que el plugin esté instalado y activado en el dispositivo que empaqueta la aplicación; si no es posible, el empaquetado termina igualmente y el diálogo final indica los plugins que quedaron fuera y qué hacer.
- La aplicación empaquetada usa primero la copia que lleva, y el plugin instalado en el dispositivo cuando no lleva ninguna.

******

### Cuando el plugin no está disponible

******

Cuando el plugin no está instalado, no está autorizado, está desactivado en el Centro de plugins o es demasiado antiguo, los scripts no pueden usar estas clases:

- Nombrar una clase no es un error por sí mismo (Rhino trata un nombre que no encuentra como un paquete Java); el error llega cuando un script llama a un método, construye un objeto o pasa el nombre a `importClass`.
- Cuando un script termina con un error así, AutoJs6 muestra en la consola, después del error, indicaciones para instalar, autorizar, activar o actualizar el plugin; los errores que el propio script captura no reciben indicaciones.
- Cuando una aplicación empaquetada no lleva ninguna copia y su dispositivo tampoco tiene un plugin utilizable, las indicaciones piden instalar y activar el plugin en el dispositivo.

******

### Compatibilidad

******

- Requiere AutoJs6 6.8.1 (build 6640) o posterior. AutoJs6 6.7.0 y versiones anteriores incluyen estas bibliotecas por sí mismas y no necesitan el plugin; AutoJs6 6.8.0 no puede usar el plugin.
- Compatible con Android 7.0 (API 24) o posterior, con un solo paquete para todas las arquitecturas de procesador.
- Las versiones de las bibliotecas son las que incluía AutoJs6 6.7.0, así que los scripts existentes no necesitan cambios.
- Como en AutoJs6 6.7.0, las bibliotecas pasan por core library desugaring: los métodos con parámetros o resultados `java.time` usan los tipos desazucarados `j$.time`.
- `java.lang.Class.forName` no encuentra las clases del plugin por su nombre; refiéralas por su nombre completo o use `importClass`.

******

### Tamaño Del Paquete

******

El paquete release ocupa unos 1.0 MB. Los jars de las tres bibliotecas suman unos 2,9 MB; el paquete conserva cada clase con su nombre original, y el dex con su conjunto de constantes compartido y la compresión lo hacen más pequeño.

******

### Preguntas Frecuentes

******

#### Por qué AutoJs6 ya no incluye estas bibliotecas?

Solo algunos scripts las usan. Como plugin, el paquete de AutoJs6 es más pequeño y quien las necesite instala el plugin. Las bibliotecas del plugin son las versiones que incluía AutoJs6 6.7.0.

#### Por qué no actualizar a versiones más nuevas de las bibliotecas?

Las versiones más nuevas ya no admiten Android 7.x: Jackson exige Android 8.0 desde 2.14, y las versiones más nuevas de Commons IO fallan en Android 7.x. Para funcionar en todos los dispositivos que admite AutoJs6 y seguir igual para los scripts existentes, el plugin mantiene las versiones de AutoJs6 6.7.0.

#### El plugin se conecta a Internet o se ejecuta en segundo plano?

No. El plugin no pide ningún permiso, y ejecutar un script nunca inicia el proceso del plugin; las clases se ejecutan en AutoJs6, que ejecuta el script.

#### El error dice que hay que autorizar el plugin. Qué hago?

La versión publicada, instalada desde el Centro de plugins de AutoJs6 o desde las Releases de este proyecto, no necesita autorización. Una compilación propia tiene otra firma y primero hay que autorizarla en el Centro de plugins según las indicaciones.

#### Otras bibliotecas que AutoJs6 ya no incluye (como ICU4J) se unirán a este plugin?

Por ahora no. ICU4J es muy grande y la mayoría de sus usos pueden pasar a `android.icu` del sistema, y Log4j 1.x ya no se mantiene. Cuando un script usa estas bibliotecas, AutoJs6 explica que ya no están incluidas.

******

### Interfaz Del Plugin

******

La siguiente información está dirigida a desarrolladores del host AutoJs6 y de plugins; el host usa estos identificadores para descubrir el plugin y cargar sus clases:

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

### Hoja De Ruta

******

La lista de comprobación previa a la publicación, las decisiones y los pasos de publicación se registran en ROADMAP.md; el host carga las clases con el paquete `org.autojs.autojs.core.plugin.javapackage` del repositorio de AutoJs6.

- [Ver ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### Historial De Versiones

******

#### v1.0.0

_2026/11/05_

- `Aviso` Requiere AutoJs6 6.8.1 (build 6640) o posterior
- `Aviso` Desde AutoJs6 6.8.0, Apache Commons Lang3, Commons IO y Jackson ya no vienen incluidos; con este plugin instalado y activado, los scripts usan las clases de estas bibliotecas por su nombre completo como con AutoJs6 6.7.0, y los scripts existentes no necesitan cambios
- `Función` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 y Jackson 2.13.4.2 (core y annotations 2.13.4), las versiones que incluía AutoJs6 6.7.0
- `Función` El plugin es solo un paquete de recursos: AutoJs6 carga las clases directamente del plugin, ejecutar scripts nunca inicia el proceso del plugin y el plugin no pide ningún permiso
- `Función` Las aplicaciones empaquetadas con AutoJs6 pueden llevar estas clases (encontradas analizando los scripts, o marcadas en la lista de bibliotecas) y usarlas después en dispositivos sin el plugin
- `Función` La página del plugin muestra la versión, la licencia y el paquete Java de cada biblioteca, un ejemplo, los archivos NOTICE y el historial de versiones
- `Función` La página del plugin sigue el idioma, el modo nocturno y el color de tema de AutoJs6, y el sistema cuando AutoJs6 no está instalado o no se pueden leer sus ajustes
- `Dependencia` Se agrega AutoJs6 Common Plugin API
- `Dependencia` Se agrega desugar_jdk_libs_nio 2.1.5

##### Para más historial de versiones

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-es.md)

******

### Compilación

******

Esta sección está dirigida a desarrolladores que quieran compilar el plugin desde el código fuente.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### Licencia

******

El código del plugin usa la [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE). Las tres bibliotecas también usan la Apache License 2.0, con sus archivos NOTICE en [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md); el paquete también contiene el runtime de core library desugaring de Android (GNU GPL v2 con la Classpath Exception).

******

### Enlaces

******

- Proyecto AutoJs6: https://github.com/SuperMonster003/AutoJs6
- Avisos de terceros: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
