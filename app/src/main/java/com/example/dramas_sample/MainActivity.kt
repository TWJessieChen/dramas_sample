package com.example.dramas_sample

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dramas_sample.application.MainApplication
import com.example.dramas_sample.database.model.DataRealm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private var progressBar: ProgressBar? = null

    private lateinit var wifiManager: WifiManager

    private var isWifiEnabled : Boolean = false

    private var dramaResults: RealmResults<DataRealm>? = null

    private val viewModel by lazy {
        MainViewModel()
    }

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById<ProgressBar>(R.id.progress_Bar) as ProgressBar
        progressBar!!.visibility = View.INVISIBLE

        viewModel.dramaList.observe(this, object : Observer<RealmResults<DataRealm>> {
            override fun onChanged(t: RealmResults<DataRealm>?) {
                dramaResults = t
                initView()
            }
        })

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    }

    private fun initView() {
        scope.launch {
            val adapter =
                MainRecyclerViewAdapter(MainApplication.appContext!!, dramaResults, object : MainRecyclerViewAdapter.OnItemClickListener {
                    override fun onItemClick(item: DataRealm) {
                        Log.d(TAG,"onItemClick: " + item.name)

                        val intent = Intent(MainApplication.appContext, DetailInfoActivity::class.java)
                        val extras = Bundle()
                        extras.putInt("EXTRA_DRAMA_ID", item.drama_id!!)
                        intent.putExtra("Bundle",extras)
                        startActivity(intent)

                    }
                }, true)

            val recyclerView : RecyclerView = findViewById(R.id.recycler_view)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(MainApplication.appContext!!)
            recyclerView.adapter = adapter

            dramaResults!!.addChangeListener(RealmChangeListener { element ->
                // data is loaded or written to/updated
                adapter.notifyDataSetChanged()
                progressBar!!.visibility = View.INVISIBLE
            })
        }
    }

    override fun onStart() {
        super.onStart()

        // Enable Wi-Fi
        if (wifiManager.isWifiEnabled) {
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
        dramaResults!!.removeAllChangeListeners()
        dramaResults = null
    }


}