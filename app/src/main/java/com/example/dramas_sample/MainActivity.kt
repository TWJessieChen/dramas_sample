package com.example.dramas_sample

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
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

/**
 *
 * 主頁面(MVVM架構設計)
 * 使用SharedPreferences來儲存query key word
 *
 * @author JC666
 */

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private val WORD = "WORD"

    private var progressBar: ProgressBar? = null

    private lateinit var wifiManager: WifiManager

    private var isWifiEnabled : Boolean = false

    private var adapter : MainRecyclerViewAdapter? = null

    private var recyclerView : RecyclerView? = null

    private var dramaResults: RealmResults<DataRealm>? = null

    private var editor: SharedPreferences.Editor? = null

    private val viewModel by lazy {
        MainViewModel()
    }

    private val scope = MainScope()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchView = menu!!.findItem(R.id.search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {

                viewModel.searchText(newText!!)

                editor!!.putString(WORD, newText).apply()

                return true
            }
        })

        searchView.setQuery(MainApplication.pref!!.getString(WORD, ""), false)

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById<ProgressBar>(R.id.progress_Bar) as ProgressBar
        progressBar!!.visibility = View.INVISIBLE

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(MainApplication.appContext!!)

        editor = MainApplication.pref!!.edit()

        val filterWord = MainApplication.pref!!.getString(WORD, "")
        if(!filterWord.equals("")) {
            viewModel.searchText(filterWord!!)
        }

        viewModel.dramaList.observe(this, object : Observer<RealmResults<DataRealm>> {
            override fun onChanged(t: RealmResults<DataRealm>?) {
                Log.d(TAG,"viewModel dramaList observe: " + t!!.size)
                dramaResults = t
                initView()

                if(!isWifiEnabled) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(R.string.warring)
                        .setMessage(R.string.intent_settings_app)
                        .setPositiveButton(R.string.ok) { _, _ ->
                            val dialogIntent = Intent(android.provider.Settings.ACTION_SETTINGS)
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(dialogIntent)
                        }
                        .setNeutralButton(R.string.cancel) { _, _ ->
                        }
                        .show()
                }

            }
        })

        viewModel.dramaFilterList.observe(this, object : Observer<RealmResults<DataRealm>> {
            override fun onChanged(t: RealmResults<DataRealm>?) {
                dramaResults = t
                adapter =
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
                recyclerView!!.swapAdapter(adapter, true)
            }
        })

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    }

    private fun initView() {
        scope.launch {
            adapter =
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

            recyclerView!!.adapter = adapter

            dramaResults!!.addChangeListener(RealmChangeListener { element ->
                // data is loaded or written to/updated
                adapter!!.notifyDataSetChanged()
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
            isWifiEnabled = false
        }

    }

    override fun onResume() {
        super.onResume()
        scope.launch {

            if(isWifiEnabled) {
                val tasks = viewModel.httpDramasDataRequest()
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