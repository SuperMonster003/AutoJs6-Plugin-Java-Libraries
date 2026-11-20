Java Libraries ofrece tres bibliotecas Java para los scripts de AutoJs6: Apache Commons Lang3, Apache Commons IO y Jackson. Una vez activado, los scripts siguen usando sus clases por su nombre completo, igual que en AutoJs6 6.7.0 y versiones anteriores.

### Primeros pasos

1. Compruebe que este plugin está activado en el Centro de plugins de AutoJs6.
2. En un script, use las clases de las bibliotecas por su nombre completo, por ejemplo `org.apache.commons.io.IOUtils`; consulte el ejemplo más abajo.
3. La página del plugin muestra la versión, los artefactos Maven y los paquetes Java de cada biblioteca; se abre desde el Centro de plugins o con el icono Java Libraries de la pantalla de inicio.

### Funciones principales

- **Bibliotecas**: la página del plugin indica la versión, los artefactos Maven, los paquetes Java y la licencia de Apache Commons Lang3, Apache Commons IO y Jackson.
- **Uso en scripts**: la página del plugin incluye un ejemplo que se puede ejecutar tal cual, con la salida de cada línea en comentarios.
- **Archivos NOTICE**: la página del plugin muestra los archivos NOTICE tal como los publican las bibliotecas.

### Uso en scripts

Use las clases por su nombre completo; los comentarios muestran la salida de cada línea:

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

- También puede llamar primero a `importClass(org.apache.commons.lang3.StringUtils)` y después escribir solo `StringUtils`.
- Cuando el plugin no está disponible, nombrar una clase no es un error por sí mismo (Rhino trata un nombre que no encuentra como un paquete Java); el error llega cuando un script llama a un método, construye un objeto o pasa el nombre a `importClass`.

### Requisitos

- Android 7.0 o posterior, y AutoJs6 6.8.1 o posterior.
- Este plugin es solo un paquete de recursos: los scripts nunca inician su proceso y no necesita ningún permiso; los scripts que no usan estas clases no se ven afectados.

### Preguntas frecuentes

**Un script no puede usar estas clases**

Compruebe que este plugin está instalado y activado en el Centro de plugins, y que AutoJs6 es la versión 6.8.1 o posterior. Cuando un script termina con un error así, AutoJs6 indica en la consola, después del error, si hay que instalar, autorizar, activar o actualizar el plugin; los errores que el propio script captura con `catch` no llevan esta indicación.

**Uso en aplicaciones empaquetadas**

Cuando AutoJs6 empaqueta un proyecto, lleva consigo las clases de este plugin si los scripts contienen un nombre que empieza por `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson`; si el análisis no encuentra ese nombre, marque Java Libraries en la lista de bibliotecas de la página de empaquetado. Una aplicación que las lleva puede usar estas clases en dispositivos sin este plugin; si no, usa el plugin instalado en el dispositivo.

El código fuente y las licencias de terceros están en la [página del proyecto](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
