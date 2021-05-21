# Android Dramas Sample

This is Android Dramas Sample code(MVVM架構設計).

## 架構設計上的概論
1.MVVM，ViewModel 搭配 Coroutine 架構  
2.使用Relam 儲存資料，目的解決無網路下，還能正常顯示資料  
3.使用SharedPreferences，用於在暫存前一次所使用app的狀態資訊(search word)  
4.使用gildory作者開源的sdk，目的讓okhttp可以跟Coroutine搭配使用，形成blocking狀態 
5.使用relam提供的RealmRecyclerViewAdapter元件，當database內容有異動時，可以隨時更新，使用在圖片下載完，可以即時更新 


## 架構設計上的缺點
1.因為viewModelScope是使用main thread，所以在使用relam上，不能存取資料量太大的IO操控
2.在filter word設計上，使用relam下filter search，但是只能用swapAdapter方式更新adapter，達到更新頁面 


## Implementation sdk lists
1.kotlinx-coroutines  
2.lifecycle-viewmodel  
3.lifecycle-livedata  
4.squareup.okhttp3:okhttp  
5.github.bumptech.glide:glide  
6.io.realm:android-adapters  
7.google.code.gson  
8.ru.gildor.coroutines:kotlin-coroutines-okhttp:1.0  


Happy Fun, Enjoy the Android Dramas Sample code.
