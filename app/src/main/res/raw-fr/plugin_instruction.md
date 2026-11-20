Java Libraries fournit trois bibliothèques Java aux scripts AutoJs6: Apache Commons Lang3, Apache Commons IO et Jackson. Une fois le plugin activé, les scripts continuent d'utiliser leurs classes par leur nom complet, comme dans AutoJs6 6.7.0 et les versions antérieures.

### Prise en main

1. Vérifiez que ce plugin est activé dans le Centre des plugins d'AutoJs6.
2. Dans un script, utilisez les classes des bibliothèques par leur nom complet, par exemple `org.apache.commons.io.IOUtils`; voir l'exemple ci-dessous.
3. La page du plugin indique la version, les artefacts Maven et les paquets Java de chaque bibliothèque; elle s'ouvre depuis le Centre des plugins ou l'icône Java Libraries de l'écran d'accueil.

### Fonctionnalités principales

- **Bibliothèques**: la page du plugin liste la version, les artefacts Maven, les paquets Java et la licence d'Apache Commons Lang3, d'Apache Commons IO et de Jackson.
- **Utilisation dans les scripts**: la page du plugin propose un exemple exécutable tel quel, avec la sortie de chaque ligne en commentaire.
- **Fichiers NOTICE**: la page du plugin affiche les fichiers NOTICE tels que les bibliothèques les publient.

### Utilisation dans les scripts

Utilisez les classes par leur nom complet; les commentaires indiquent la sortie de chaque ligne:

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

- Vous pouvez aussi appeler d'abord `importClass(org.apache.commons.lang3.StringUtils)`, puis écrire simplement `StringUtils`.
- Quand le plugin n'est pas disponible, nommer une classe n'est pas une erreur en soi (Rhino traite un nom introuvable comme un paquet Java); l'erreur survient quand un script appelle une méthode, construit un objet ou passe le nom à `importClass`.

### Prérequis

- Android 7.0 ou version ultérieure, et AutoJs6 6.8.1 ou version ultérieure.
- Ce plugin n'est qu'un paquet de ressources: les scripts ne démarrent jamais son processus et il ne demande aucune autorisation; les scripts qui n'utilisent pas ces classes ne sont pas concernés.

### Questions fréquentes

**Un script ne peut pas utiliser ces classes**

Vérifiez que ce plugin est installé et activé dans le Centre des plugins, et qu'AutoJs6 est en version 6.8.1 ou ultérieure. Quand un script se termine sur une telle erreur, AutoJs6 indique dans la console, après l'erreur, s'il faut installer, autoriser, activer ou mettre à jour le plugin; les erreurs que le script intercepte lui-même avec `catch` n'ont pas cette indication.

**Utilisation dans les applications empaquetées**

Quand AutoJs6 empaquette un projet, il emporte les classes de ce plugin si les scripts contiennent un nom qui commence par `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson`; si l'analyse ne trouve pas un tel nom, cochez Java Libraries dans la liste des bibliothèques de la page d'empaquetage. Une application qui les emporte peut utiliser ces classes sur les appareils sans ce plugin; sinon, elle utilise le plugin installé sur l'appareil.

Le code source et les licences tierces se trouvent sur la [page du projet](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries).
