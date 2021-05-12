package com.example.dramas_sample

import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dramas_sample.application.MainApplication
import com.example.dramas_sample.database.model.DataRealm
import com.example.dramas_sample.utils.BitmapAndBase64StringToolUtil
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DetailInfoActivity : AppCompatActivity() {
    private val TAG = DetailInfoActivity::class.java.simpleName

    private val viewModel by lazy {
        MainViewModel()
    }

    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_info)

        val bundle= intent.getBundleExtra("Bundle")
        val dramaId = bundle?.getInt("EXTRA_DRAMA_ID")

        viewModel.dramaList.observe(this, object : Observer<RealmResults<DataRealm>> {
            override fun onChanged(t: RealmResults<DataRealm>?) {
                scope.launch {

                    Log.d(TAG,"Drama id: " + dramaId)

                    val iv_thumb: ImageView = findViewById(R.id.iv_thumb)
                    val tv_name: TextView = findViewById(R.id.tv_name)
                    val tv_createdAt: TextView = findViewById(R.id.tv_createdAt)
                    val tv_rating: TextView = findViewById(R.id.tv_rating)

                    for(data in t!!) {
                        if(data.drama_id!!.equals(dramaId)) {
                            iv_thumb.setImageBitmap(BitmapAndBase64StringToolUtil.convertStringToBitmap(data.base64Thumb!!))
                            tv_name.text = data.name
                            tv_createdAt.text = data.created_at
                            tv_rating.text = data.rating!!.toBigDecimal().toPlainString()
                        }
                    }
                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
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