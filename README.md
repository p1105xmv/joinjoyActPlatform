# Joinjoy Readme


<br>

## Joinjoy揪活動平台


- [關於本專案](https://github.com/p1105xmv/joinjoyActPlatform/tree/main?tab=readme-ov-file#%E9%97%9C%E6%96%BC%E6%9C%AC%E5%B0%88%E6%A1%88)
- [畫面](https://github.com/p1105xmv/joinjoyActPlatform/tree/main?tab=readme-ov-file#%E7%95%AB%E9%9D%A2)
- [專案技術](https://github.com/p1105xmv/joinjoyActPlatform/tree/main?tab=readme-ov-file#%E5%B0%88%E6%A1%88%E6%8A%80%E8%A1%93)
- [如何開始](https://github.com/p1105xmv/joinjoyActPlatform/tree/main?tab=readme-ov-file#%E5%A6%82%E4%BD%95%E9%96%8B%E5%A7%8B)
  
  

<br>
  
## 關於本專案



JoinJoy 是一個活動平台，使用者可以報名參與活動、發起活動以及發起討論文章，透過共同平台讓有相同興趣的各位有更多相遇的機會。


<br>


## 畫面

會員專區

<img width="1318" alt="會員專區" src="https://github.com/p1105xmv/joinjoyActPlatform/assets/154125690/70c8c4a8-fe5c-4731-8b72-e221aec9146c">

查看會員已收藏的活動

<img width="1321" alt="收藏的活動" src="https://github.com/p1105xmv/joinjoyActPlatform/assets/154125690/1c04b05b-5d63-43e6-a6cb-90c1c015d834">


<br>
<br>

## 專案技術


Java版本為jdk17

資料庫：MSSQL、Redis

開發工具：SpringBoot(Spring Tool Suites 4)

版控工具：Github、SourceTree

其他：Vue.js、Bootstrap、Croppie、Google OAuth、jquery


<br>

## 如何開始



1. 下載完專案後，將「資料庫insert檔」資料貼到自己的MSSQL當中，產生新的資料庫及資料表
2. 至專案中的src/main/resources，找到application.properties設定檔：
    
    填入資料庫連線的url、帳號及密碼
    
    spring.datasource.url=
    spring.datasource.username=
    spring.datasource.password=
    
    (如果是sql server 的話，url可以寫 —>jdbc:sqlserver://localhost:1433;databaseName=”你的資料庫名”;trustServerCertificate=true)
    
3. 如要測試Spring Mail自動發送通知信功能，同樣在application.properties設定檔設定spring mail 的指定信箱，以及其授權碼：
    
    #發送方帳號
    
    spring.mail.username=”填入信箱”
    
    #發送方密碼（授權碼）
    
    spring.mail.password=“填入該信箱取得的一組授權碼”
    
4. 啟動程式後，在瀏覽器的網頁輸入[http://localhost:8081](http://localhost:8081/)，即可以啟動網頁。
