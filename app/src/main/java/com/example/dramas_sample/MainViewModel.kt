package com.example.dramas_sample

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dramas_sample.http.DramaContact
//import com.example.dramas_sample.database.DataRealm
import com.example.dramas_sample.http.DramaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainViewModel  : ViewModel() {
    private val TAG = MainViewModel::class.java.simpleName

//    val dramas = MutableLiveData<DataRealm>()

    suspend fun httpDramasDataRequest() {
        withContext(Dispatchers.IO) {

            val client = OkHttpClient()  // preinitialize the client

            val req = Request.Builder().url(DramaContact.BASE_URL).build()
            val res = client.newCall(req).execute()

            Log.d(TAG, "" + res.body)
            Log.d(TAG, "" + res.body)

        }
    }



}