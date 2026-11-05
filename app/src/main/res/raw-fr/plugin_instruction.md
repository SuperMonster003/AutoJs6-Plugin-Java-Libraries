Java Libraries remet sur l'appareil les trois bibliothèques Java qu'AutoJs6 intégrait autrefois: Apache Commons Lang3, Apache Commons IO et Jackson. Depuis AutoJs6 6.8.0, le paquet d'AutoJs6 ne les contient plus; une fois ce plugin installé et activé dans AutoJs6 6.8.1 ou ultérieur, les scripts utilisent par leur nom complet des classes comme `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils` ou `com.fasterxml.jackson.databind.ObjectMapper`, exactement comme avec AutoJs6 6.7.0 et les versions antérieures.

Le plugin n'est qu'un paquet de ressources: AutoJs6 charge les classes directement depuis le paquet du plugin, les scripts ne démarrent jamais le processus du plugin et le plugin ne demande aucune autorisation. Les scripts qui n'utilisent pas ces classes ne sont pas concernés, et les classes inutilisées ne sont jamais chargées; elles n'occupent que de la place dans le paquet.

| Bibliothèque | Artefacts Maven et versions | Paquet Java | Licence |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

### Utilisation

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

### Quand le plugin est indisponible

Quand le plugin n'est pas installé, pas autorisé, désactivé dans le Centre des plugins ou trop ancien, les scripts ne peuvent pas utiliser ces classes:

- Nommer une classe n'est pas une erreur en soi (Rhino traite un nom introuvable comme un paquet Java); l'erreur survient quand un script appelle une méthode, construit un objet ou passe le nom à `importClass`.
- Quand un script se termine sur une telle erreur, AutoJs6 affiche dans la console, après l'erreur, des indications pour installer, autoriser, activer ou mettre à jour le plugin; les erreurs que le script intercepte lui-même n'en reçoivent pas.
- Quand une application empaquetée n'emporte aucune copie et que son appareil n'a pas non plus de plugin utilisable, les indications demandent d'installer et d'activer le plugin sur l'appareil.

Le code source, l'historique des versions et les licences tierces se trouvent sur la [page du projet](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
