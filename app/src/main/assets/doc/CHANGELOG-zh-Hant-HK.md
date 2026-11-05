******

### 發行歷史

******

# v1.0.0

###### 2026/11/05

* `提示` 需要 AutoJs6 6.8.1 (構建 6640) 或更高版本
* `提示` AutoJs6 6.8.0 起不再內置 Apache Commons Lang3, Commons IO 與 Jackson; 安裝並啟用本插件後, 腳本照常以全名使用這些庫的類, 寫法與 AutoJs6 6.7.0 相同, 原有腳本無需修改
* `新增` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 與 Jackson 2.13.4.2 (core 與 annotations 為 2.13.4), 版本與 AutoJs6 6.7.0 內置的相同
* `新增` 插件只是一個資源包: AutoJs6 直接從插件載入這些類, 腳本運行時不啟動插件進程, 插件不申請任何權限
* `新增` 用 AutoJs6 打包應用時可以帶入這些類 (自動掃描腳本, 也可以在庫列表中勾選), 打包後的應用在沒有本插件的裝置上也能使用
* `新增` 插件頁面顯示各庫的版本, 授權, Java 包, 使用範例, NOTICE 文件與發行歷史
* `新增` 插件頁面跟隨 AutoJs6 的語言, 夜間模式與主題色, 未安裝 AutoJs6 或無法讀取其設定時跟隨系統
* `依賴` 附加 AutoJs6 Common Plugin API
* `依賴` 附加 desugar_jdk_libs_nio 2.1.5
