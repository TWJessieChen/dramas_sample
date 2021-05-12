package com.example.dramas_sample

//import com.example.dramas_sample.database.DataRealm

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import com.example.dramas_sample.data.Data
import com.example.dramas_sample.data.DramaList
import com.example.dramas_sample.http.DramaContact
import com.example.dramas_sample.http.HttpGetMethod
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException


class MainViewModel  : ViewModel() {
    private val TAG = MainViewModel::class.java.simpleName

//    val dramas = MutableLiveData<DataRealm>()

    suspend fun httpDramasDataRequest() {
        withContext(Dispatchers.IO) {

            val resultList = HttpGetMethod.getDramasDataRequest()

            Log.d(TAG, "" + resultList.data.size)



        }
    }



}