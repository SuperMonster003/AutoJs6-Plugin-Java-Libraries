Java Libraries devuelve al dispositivo las tres bibliotecas Java que AutoJs6 incluía antes: Apache Commons Lang3, Apache Commons IO y Jackson. Desde AutoJs6 6.8.0, el paquete de AutoJs6 ya no las incluye; con este plugin instalado y activado en AutoJs6 6.8.1 o posterior, los scripts usan por su nombre completo clases como `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils` o `com.fasterxml.jackson.databind.ObjectMapper`, igual que con AutoJs6 6.7.0 y versiones anteriores.

El plugin es solo un paquete de recursos: AutoJs6 carga las clases directamente del paquete del plugin, los scripts nunca inician el proceso del plugin y el plugin no pide ningún permiso. Los scripts que no usan estas clases no se ven afectados, y las clases que nadie usa nunca se cargan; solo ocupan espacio en el paquete.

| Biblioteca | Artefactos Maven y versiones | Paquete Java | Licencia |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

### Uso

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

### Cuando el plugin no está disponible

Cuando el plugin no está instalado, no está autorizado, está desactivado en el Centro de plugins o es demasiado antiguo, los scripts no pueden usar estas clases:

- Nombrar una clase no es un error por sí mismo (Rhino trata un nombre que no encuentra como un paquete Java); el error llega cuando un script llama a un método, construye un objeto o pasa el nombre a `importClass`.
- Cuando un script termina con un error así, AutoJs6 muestra en la consola, después del error, indicaciones para instalar, autorizar, activar o actualizar el plugin; los errores que el propio script captura no reciben indicaciones.
- Cuando una aplicación empaquetada no lleva ninguna copia y su dispositivo tampoco tiene un plugin utilizable, las indicaciones piden instalar y activar el plugin en el dispositivo.

El código fuente, el historial de versiones y las licencias de terceros están en la [página del proyecto](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
