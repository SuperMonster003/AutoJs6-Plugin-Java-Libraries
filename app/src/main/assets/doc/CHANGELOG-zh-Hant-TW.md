******

### 發行歷史

******

# v1.0.0

###### 2026/11/05

* `提示` 需要 AutoJs6 6.8.1 (建置 6640) 或更新版本
* `提示` AutoJs6 6.8.0 起不再內建 Apache Commons Lang3, Commons IO 與 Jackson; 安裝並啟用本外掛後, 腳本照常以全名使用這些函式庫的類別, 寫法與 AutoJs6 6.7.0 相同, 原有腳本無需修改
* `新增` Apache Commons Lang3 3.18.0, Apache Commons IO 2.8.0 與 Jackson 2.13.4.2 (core 與 annotations 為 2.13.4), 版本與 AutoJs6 6.7.0 內建的相同
* `新增` 外掛只是一個資源套件: AutoJs6 直接從外掛載入這些類別, 腳本執行時不啟動外掛處理程序, 外掛不要求任何權限
* `新增` 用 AutoJs6 打包應用程式時可以帶入這些類別 (自動掃描腳本, 也可以在函式庫清單中勾選), 打包後的應用程式在沒有本外掛的裝置上也能使用
* `新增` 外掛頁面顯示各函式庫的版本, 授權, Java 套件, 使用範例, NOTICE 檔案與發行歷史
* `新增` 外掛頁面跟隨 AutoJs6 的語言, 夜間模式與主題色, 未安裝 AutoJs6 或無法讀取其設定時跟隨系統
* `相依性` 附加 AutoJs6 Common Plugin API
* `相依性` 附加 desugar_jdk_libs_nio 2.1.5
