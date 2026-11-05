<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-java-libraries-ic-launcher" border="0" width="128" />
  </p>

  <p>Apache Commons Lang3, Commons IO et Jackson à utiliser directement comme classes Java dans les scripts AutoJs6</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Java-Libraries?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Java-Libraries?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Langues (Languages)

******

Le README.md actuel prend en charge les langues suivantes:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-en.md)
- Français [fr] # actuel
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/.readme/README-ar.md)

******

### Introduction

******

Java Libraries remet sur l'appareil les trois bibliothèques Java qu'AutoJs6 intégrait autrefois: Apache Commons Lang3, Apache Commons IO et Jackson. Depuis AutoJs6 6.8.0, le paquet d'AutoJs6 ne les contient plus; une fois ce plugin installé et activé dans AutoJs6 6.8.1 ou ultérieur, les scripts utilisent par leur nom complet des classes comme `org.apache.commons.lang3.StringUtils`, `org.apache.commons.io.IOUtils` ou `com.fasterxml.jackson.databind.ObjectMapper`, exactement comme avec AutoJs6 6.7.0 et les versions antérieures.

Le plugin n'est qu'un paquet de ressources: AutoJs6 charge les classes directement depuis le paquet du plugin, les scripts ne démarrent jamais le processus du plugin et le plugin ne demande aucune autorisation. Les scripts qui n'utilisent pas ces classes ne sont pas concernés, et les classes inutilisées ne sont jamais chargées; elles n'occupent que de la place dans le paquet.

******

### Bibliothèques incluses

******

Les classes de chaque bibliothèque se trouvent dans le paquet Java du tableau ci-dessous, dans les versions qu'intégrait AutoJs6 6.7.0. Les licences et fichiers NOTICE des bibliothèques figurent dans [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md), et la page du plugin les affiche aussi.

| Bibliothèque | Artefacts Maven et versions | Paquet Java | Licence |
|---|---|---|---|
| [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) | `org.apache.commons:commons-lang3` 3.18.0 | `org.apache.commons.lang3` | Apache-2.0 |
| [Apache Commons IO](https://commons.apache.org/proper/commons-io/) | `commons-io:commons-io` 2.8.0 | `org.apache.commons.io` | Apache-2.0 |
| [Jackson](https://github.com/FasterXML/jackson) | `com.fasterxml.jackson.core:jackson-databind` 2.13.4.2, `com.fasterxml.jackson.core:jackson-core` 2.13.4, `com.fasterxml.jackson.core:jackson-annotations` 2.13.4 | `com.fasterxml.jackson` | Apache-2.0 |

******

### Utilisation

******

1. Utilisez AutoJs6 6.8.1 (build 6640) ou ultérieur.
2. Installez Java Libraries depuis le Centre des plugins d'AutoJs6 (ou téléchargez l'APK depuis [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/releases)) et vérifiez que le plugin est activé.
3. Utilisez les classes des bibliothèques par leur nom complet dans les scripts (ou après `importClass` ou `importPackage`), par exemple:

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

> Il n'existe qu'un seul paquet universel, sans bibliothèque native, pour les appareils sous Android 7.0 (API 24) ou ultérieur.

******

### Applications empaquetées

******

Quand AutoJs6 empaquette des scripts en application autonome, il peut emporter les classes de ce plugin, afin que l'application empaquetée puisse utiliser les bibliothèques sur les appareils sans le plugin:

- Pendant l'empaquetage, AutoJs6 analyse les scripts du projet et emporte les classes dès qu'il trouve un nom qui commence par `org.apache.commons.lang3`, `org.apache.commons.io`, `com.fasterxml.jackson` (les scripts chiffrés et les fichiers de plus de 4 Mo ne sont pas analysés).
- Quand un nom est assemblé à partir de chaînes et que l'analyse ne le trouve pas, cochez Java Libraries dans la liste des bibliothèques de la page d'empaquetage.
- Emporter les classes demande que le plugin soit installé et activé sur l'appareil qui empaquette l'application; si ce n'est pas possible, l'empaquetage se termine quand même et la boîte de dialogue finale indique les plugins laissés de côté et la marche à suivre.
- L'application empaquetée utilise d'abord la copie qu'elle emporte, et le plugin installé sur l'appareil quand elle n'en emporte pas.

******

### Quand le plugin est indisponible

******

Quand le plugin n'est pas installé, pas autorisé, désactivé dans le Centre des plugins ou trop ancien, les scripts ne peuvent pas utiliser ces classes:

- Nommer une classe n'est pas une erreur en soi (Rhino traite un nom introuvable comme un paquet Java); l'erreur survient quand un script appelle une méthode, construit un objet ou passe le nom à `importClass`.
- Quand un script se termine sur une telle erreur, AutoJs6 affiche dans la console, après l'erreur, des indications pour installer, autoriser, activer ou mettre à jour le plugin; les erreurs que le script intercepte lui-même n'en reçoivent pas.
- Quand une application empaquetée n'emporte aucune copie et que son appareil n'a pas non plus de plugin utilisable, les indications demandent d'installer et d'activer le plugin sur l'appareil.

******

### Compatibilité

******

- Nécessite AutoJs6 6.8.1 (build 6640) ou ultérieur. AutoJs6 6.7.0 et les versions antérieures intègrent eux-mêmes ces bibliothèques et n'ont pas besoin du plugin; AutoJs6 6.8.0 ne peut pas utiliser le plugin.
- Prend en charge Android 7.0 (API 24) ou ultérieur, avec un seul paquet pour toutes les architectures de processeur.
- Les versions des bibliothèques sont celles qu'intégrait AutoJs6 6.7.0: les scripts existants n'ont rien à changer.
- Comme dans AutoJs6 6.7.0, les bibliothèques passent par le core library desugaring: les méthodes avec des paramètres ou résultats `java.time` utilisent les types désucrés `j$.time`.
- `java.lang.Class.forName` ne trouve pas les classes du plugin par leur nom; référencez-les par leur nom complet ou utilisez `importClass`.

******

### Taille Du Paquet

******

Le paquet release fait environ 1.0 Mo. Les jars des trois bibliothèques totalisent environ 2,9 Mo; le paquet garde chaque classe sous son nom d'origine, et le dex avec son pool de constantes partagé ainsi que la compression le rendent plus petit.

******

### FAQ

******

#### Pourquoi AutoJs6 n'intègre-t-il plus ces bibliothèques?

Seuls certains scripts les utilisent. Sous forme de plugin, elles allègent le paquet d'AutoJs6, et ceux qui en ont besoin installent le plugin. Les bibliothèques du plugin sont dans les versions qu'intégrait AutoJs6 6.7.0.

#### Pourquoi ne pas passer à des versions plus récentes des bibliothèques?

Les versions plus récentes ne prennent plus en charge Android 7.x: Jackson exige Android 8.0 à partir de 2.14, et les versions plus récentes de Commons IO échouent sous Android 7.x. Pour fonctionner sur tous les appareils pris en charge par AutoJs6 et rester identique pour les scripts existants, le plugin garde les versions d'AutoJs6 6.7.0.

#### Le plugin se connecte-t-il à Internet ou tourne-t-il en arrière-plan?

Non. Le plugin ne demande aucune autorisation, et exécuter un script ne démarre jamais le processus du plugin; les classes s'exécutent dans AutoJs6, qui exécute le script.

#### L'erreur indique que le plugin doit être autorisé. Que faire?

La version publiée, installée depuis le Centre des plugins d'AutoJs6 ou depuis les Releases de ce projet, n'a besoin d'aucune autorisation. Une version compilée soi-même a une autre signature et doit d'abord être autorisée dans le Centre des plugins, comme indiqué.

#### D'autres bibliothèques qu'AutoJs6 n'intègre plus (comme ICU4J) rejoindront-elles ce plugin?

Pas pour l'instant. ICU4J est très volumineux et la plupart de ses usages peuvent passer à `android.icu` du système, et Log4j 1.x n'est plus maintenu. Quand un script utilise ces bibliothèques, AutoJs6 explique qu'elles ne sont plus intégrées.

******

### Interface Du Plugin

******

Les informations suivantes s'adressent aux développeurs de l'hôte AutoJs6 et de plugins; l'hôte utilise ces identifiants pour découvrir le plugin et charger ses classes:

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

### Feuille De Route

******

La liste de contrôle avant publication, les décisions et les étapes de publication sont suivies dans ROADMAP.md; l'hôte charge les classes avec le paquet `org.autojs.autojs.core.plugin.javapackage` du dépôt AutoJs6.

- [Voir ROADMAP.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/ROADMAP.md)

******

### Historique Des Versions

******

#### v1.0.0

_2026/11/05_

- `Note` Nécessite AutoJs6 6.8.1 (build 6640) ou ultérieur
- `Note` Depuis AutoJs6 6.8.0, Apache Commons Lang3, Commons IO et Jackson ne sont plus intégrés; avec ce plugin installé et activé, les scripts utilisent les classes de ces bibliothèques par leur nom complet comme avec AutoJs6 6.7.0, et les scripts existants n'ont rien à changer
- `Fonctionnalité` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 et Jackson 2.13.4.2 (core et annotations 2.13.4), les versions qu'intégrait AutoJs6 6.7.0
- `Fonctionnalité` Le plugin n'est qu'un paquet de ressources: AutoJs6 charge les classes directement depuis le plugin, exécuter un script ne démarre jamais le processus du plugin, et le plugin ne demande aucune autorisation
- `Fonctionnalité` Les applications empaquetées avec AutoJs6 peuvent emporter ces classes (trouvées en analysant les scripts, ou cochées dans la liste des bibliothèques) et les utiliser ensuite sur les appareils sans le plugin
- `Fonctionnalité` La page du plugin affiche la version, la licence et le paquet Java de chaque bibliothèque, un exemple, les fichiers NOTICE et l'historique des versions
- `Fonctionnalité` La page du plugin suit la langue, le mode nuit et la couleur de thème d'AutoJs6, et le système quand AutoJs6 n'est pas installé ou que ses réglages sont illisibles
- `Dépendance` Ajout de AutoJs6 Common Plugin API
- `Dépendance` Ajout de desugar_jdk_libs_nio 2.1.5

##### Pour plus d'historique des versions

* [CHANGELOG.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/app/src/main/assets/doc/CHANGELOG-fr.md)

******

### Compilation

******

Cette section s'adresse aux développeurs souhaitant compiler le plugin depuis les sources.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
py .python\generate_markdown.py --check
py .python\generate_icon_studio.py --check
py .python\check_repository.py
```

******

### Licence

******

Le code du plugin est sous [Apache License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/LICENSE). Les trois bibliothèques sont elles aussi sous Apache License 2.0, avec leurs fichiers NOTICE dans [THIRD_PARTY_NOTICES.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md); le paquet contient aussi le runtime du core library desugaring d'Android (GNU GPL v2 avec la Classpath Exception).

******

### Liens

******

- Projet AutoJs6: https://github.com/SuperMonster003/AutoJs6
- Mentions des tiers: https://github.com/SuperMonster003/AutoJs6-Plugin-Java-Libraries/blob/master/THIRD_PARTY_NOTICES.md
