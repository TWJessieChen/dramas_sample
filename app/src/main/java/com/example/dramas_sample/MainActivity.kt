package com.example.dramas_sample

import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dramas_sample.database.JCDatabaseManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var wifiManager: WifiManager

    private var isWifiEnabled : Boolean = false


    private val viewModel by lazy {
        MainViewModel()
    }

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    }

    override fun onStart() {
        super.onStart()

        // Enable Wi-Fi
        if (wifiManager.isWifiEnabled == false) {
            Toast.makeText(this, R.string.prompt_enabling_wifi, Toast.LENGTH_SHORT).show()
            isWifiEnabled = true
        } else {
            Toast.makeText(this, R.string.prompt_disabling_wifi, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()
        scope.launch {

            if(isWifiEnabled) {
                val tasks = viewModel.httpDramasDataRequest()
            } else {
                //當沒有網路時，走Load database流程

            }

        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }


}