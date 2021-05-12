package com.example.dramas_sample.application

import android.app.Application
import android.content.ContentResolver
import android.content.Context
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

    }


    companion object {
        private val TAG = MainApplication::class.java.simpleName
        var appContentResolver: ContentResolver? = null
            private set
        var appContext: Context? = null

    }


}