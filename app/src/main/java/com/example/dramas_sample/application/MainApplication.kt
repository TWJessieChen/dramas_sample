package com.example.dramas_sample.application

import android.app.Application
import android.util.Log
//import io.realm.Realm

class MainApplication  : Application() {
    private val TAG = MainApplication::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        //Realm init
//        Realm.init(this)
    }





}