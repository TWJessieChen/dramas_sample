package com.example.dramas_sample

import android.content.Context
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*

import androidx.recyclerview.widget.RecyclerView;
import com.example.dramas_sample.database.model.DataRealm
import com.example.dramas_sample.utils.BitmapAndBase64StringToolUtil
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

class MainRecyclerViewAdapter(private val context: Context,
                              private var dataList: OrderedRealmCollection<DataRealm>?,
                              private var listener: OnItemClickListener,
                              private val autoUpdate: Boolean) :
    RealmRecyclerViewAdapter<DataRealm, MainRecyclerViewAdapter.MainRecyclerViewHolder>(dataList, autoUpdate) {

    private val TAG = MainRecyclerViewAdapter::class.java.simpleName

    override fun getItemCount(): Int = dataList?.size ?: 0

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        val data: DataRealm = dataList?.get(position) ?: return

        //目前不需要使用Glide方式去動態下載圖片了，改用base64方式load bitmap
//        Glide.with(context)
//            .load(data.thumb)
//            .error(R.mipmap.ic_launcher)
//            .into(holder.iv_thumb)

        holder.iv_thumb.setImageBitmap(BitmapAndBase64StringToolUtil.convertStringToBitmap(data.base64Thumb!!))

        holder.container.setOnClickListener{
            listener.onItemClick(data)
        }

        holder.tv_name.text = data.name

        holder.tv_createdAt.text = data.created_at

        holder.tv_rating.text = data.rating!!.toBigDecimal().toPlainString()

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false)
        return MainRecyclerViewHolder(v)
    }

    class MainRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container : LinearLayout = view.findViewById(R.id.container)
        val iv_thumb: ImageView = view.findViewById(R.id.iv_thumb)
        val tv_name: TextView = view.findViewById(R.id.tv_name)
        val tv_createdAt: TextView = view.findViewById(R.id.tv_createdAt)
        val tv_rating: TextView = view.findViewById(R.id.tv_rating)
    }

    interface OnItemClickListener {
        fun onItemClick(item: DataRealm)
    }

}
