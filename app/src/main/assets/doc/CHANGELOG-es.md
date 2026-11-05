******

### Historial De Versiones

******

# v1.0.0

###### 2026/11/05

* `Aviso` Requiere AutoJs6 6.8.1 (build 6640) o posterior
* `Aviso` Desde AutoJs6 6.8.0, Apache Commons Lang3, Commons IO y Jackson ya no vienen incluidos; con este plugin instalado y activado, los scripts usan las clases de estas bibliotecas por su nombre completo como con AutoJs6 6.7.0, y los scripts existentes no necesitan cambios
* `Función` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 y Jackson 2.13.4.2 (core y annotations 2.13.4), las versiones que incluía AutoJs6 6.7.0
* `Función` El plugin es solo un paquete de recursos: AutoJs6 carga las clases directamente del plugin, ejecutar scripts nunca inicia el proceso del plugin y el plugin no pide ningún permiso
* `Función` Las aplicaciones empaquetadas con AutoJs6 pueden llevar estas clases (encontradas analizando los scripts, o marcadas en la lista de bibliotecas) y usarlas después en dispositivos sin el plugin
* `Función` La página del plugin muestra la versión, la licencia y el paquete Java de cada biblioteca, un ejemplo, los archivos NOTICE y el historial de versiones
* `Función` La página del plugin sigue el idioma, el modo nocturno y el color de tema de AutoJs6, y el sistema cuando AutoJs6 no está instalado o no se pueden leer sus ajustes
* `Dependencia` Se agrega AutoJs6 Common Plugin API
* `Dependencia` Se agrega desugar_jdk_libs_nio 2.1.5
