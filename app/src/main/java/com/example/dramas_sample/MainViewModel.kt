package com.example.dramas_sample

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.example.dramas_sample.data.Data
import com.example.dramas_sample.data.DramaList
import com.example.dramas_sample.database.model.DataRealm
import com.example.dramas_sample.database.module.JCDatabaseManager
import com.example.dramas_sample.database.module.JCRealmConfiguration
import com.example.dramas_sample.http.DramaContact
import com.example.dramas_sample.http.HttpGetMethod
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException


class MainViewModel  : ViewModel() {
    private val TAG = MainViewModel::class.java.simpleName

    private var realm : Realm? = null

    private val viewModelJob = SupervisorJob()

    val dramaList = MutableLiveData<RealmResults<DataRealm>>()

    init {
        viewModelScope.launch {

            realm = JCRealmConfiguration.getDramasInstance()

            val response = JCDatabaseManager.query(realm!!)

            dramaList.postValue(response)

        }
    }

    suspend fun httpDramasDataRequest() {
        viewModelScope.launch {
            val resultList = HttpGetMethod.getDramasDataRequest()

            if(resultList.first) {
                Log.d(TAG, "" + resultList.third.data.size)

                JCDatabaseManager.insert(resultList.third, realm!!)

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