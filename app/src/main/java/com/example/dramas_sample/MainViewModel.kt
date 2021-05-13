package com.example.dramas_sample

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.dramas_sample.application.MainApplication
import com.example.dramas_sample.database.model.DataRealm
import com.example.dramas_sample.database.module.JCDatabaseManager
import com.example.dramas_sample.database.module.JCRealmConfiguration
import com.example.dramas_sample.http.HttpGetMethod
import com.example.dramas_sample.utils.BitmapAndBase64StringToolUtil
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 *
 * ViewModel架構
 * 使用LiveData
 * 使用CoroutineScope
 *
 * @author JC666
 */

class MainViewModel  : ViewModel() {
    private val TAG = MainViewModel::class.java.simpleName

    private var realm : Realm? = null

    private val viewModelJob = SupervisorJob()

    val dramaList = MutableLiveData<RealmResults<DataRealm>>()

    val dramaFilterList = MutableLiveData<RealmResults<DataRealm>>()

    val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    init {
        viewModelScope.launch {

            realm = JCRealmConfiguration.getDramasInstance()

            val response = JCDatabaseManager.query(realm!!)

            dramaList.postValue(response)

        }
    }

    fun searchText(filterStr: String) {
        viewModelScope.launch {

            var response: RealmResults<DataRealm>? = null

            if (filterStr.isEmpty()) {
                response = JCDatabaseManager.query(realm!!)
            } else {
                response = JCDatabaseManager.queryFilter(filterStr, realm!!)
            }

            dramaFilterList.postValue(response!!)
        }
    }

    suspend fun httpDramasDataRequest() {
        viewModelScope.launch {
            val resultList = HttpGetMethod.getDramasDataRequest()

            if(resultList.first) {
                Log.d(TAG, "" + resultList.third.data.size)

                JCDatabaseManager.insert(resultList.third, realm!!)

                ioScope.launch {
                    for(data in resultList.third.data) {
                        try {

                            val bitmap: Bitmap = Glide
                                .with(MainApplication.appContext!!)
                                .asBitmap()
                                .load(data.thumb)
                                .submit()
                                .get()

                            Log.d(TAG,"width: " + bitmap.width + " height: " + bitmap.height)

                            val base64Str = BitmapAndBase64StringToolUtil.convertBitmapToBase64String(bitmap)

                            viewModelScope.launch {
                                JCDatabaseManager.updateBitmap(data, base64Str!!, realm!!)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }



            } else {
                Log.d(TAG, "Error: " + resultList.second)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        realm!!.close()
        realm = null
        viewModelJob.cancel()
    }
}