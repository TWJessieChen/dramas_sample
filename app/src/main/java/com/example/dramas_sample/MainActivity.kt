package com.example.dramas_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dramas_sample.database.JCDatabaseManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private val viewModel by lazy {
        MainViewModel()
    }

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()
        scope.launch {
            val tasks = viewModel.httpDramasDataRequest()
        }
    }

    override fun onPause() {
        super.onPause()
    }




    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }


}