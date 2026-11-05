******

### Historique Des Versions

******

# v1.0.0

###### 2026/11/05

* `Note` Nécessite AutoJs6 6.8.1 (build 6641) ou ultérieur
* `Note` Depuis AutoJs6 6.8.0, Apache Commons Lang3, Commons IO et Jackson ne sont plus intégrés; avec ce plugin installé et activé, les scripts utilisent les classes de ces bibliothèques par leur nom complet comme avec AutoJs6 6.7.0, et les scripts existants n'ont rien à changer
* `Fonctionnalité` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 et Jackson 2.13.4.2 (core et annotations 2.13.4), les versions qu'intégrait AutoJs6 6.7.0
* `Fonctionnalité` Le plugin n'est qu'un paquet de ressources: AutoJs6 charge les classes directement depuis le plugin, exécuter un script ne démarre jamais le processus du plugin, et le plugin ne demande aucune autorisation
* `Fonctionnalité` Les applications empaquetées avec AutoJs6 peuvent emporter ces classes (trouvées en analysant les scripts, ou cochées dans la liste des bibliothèques) et les utiliser ensuite sur les appareils sans le plugin
* `Fonctionnalité` La page du plugin affiche la version, la licence et le paquet Java de chaque bibliothèque, un exemple, les fichiers NOTICE et l'historique des versions
* `Fonctionnalité` La page du plugin suit la langue, le mode nuit et la couleur de thème d'AutoJs6, et le système quand AutoJs6 n'est pas installé ou que ses réglages sont illisibles
* `Dépendance` Ajout de AutoJs6 Common Plugin API
* `Dépendance` Ajout de desugar_jdk_libs_nio 2.1.5
