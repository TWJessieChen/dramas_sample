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

            val client = OkHttpClient()

            val req = Request.Builder().url(DramaContact.BASE_URL).build()

            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d(TAG, "" + e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val resStr = response.body!!.string()
                    Log.d(TAG, "" + resStr)

                    val gson = GsonBuilder().create()
                    val forecast = gson.fromJson(resStr, DramaList::class.java)

                    Log.d(TAG, "" + forecast.data.size)

                }
            })

        }
    }



}