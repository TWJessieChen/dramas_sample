package com.example.dramas_sample.application

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.realm.Realm

class MainApplication  : Application() {
    private val TAG = MainApplication::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        //Realm init
        Realm.init(this)

        appContentResolver = contentResolver

        appContext = applicationContext

        pref = applicationContext.getSharedPreferences("FILTER_WORD", Context.MODE_PRIVATE)

    }


    companion object {
        private val TAG = MainApplication::class.java.simpleName
        var appContentResolver: ContentResolver? = null
            private set
        var appContext: Context? = null

        var pref: SharedPreferences? = null
    }


}