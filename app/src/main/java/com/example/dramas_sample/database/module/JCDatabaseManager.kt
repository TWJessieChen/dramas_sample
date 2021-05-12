package com.example.dramas_sample.database.module

import android.util.Log
import com.example.dramas_sample.data.Data
import com.example.dramas_sample.data.DramaList
import com.example.dramas_sample.database.model.DataRealm
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults

object JCDatabaseManager {
    private val TAG = JCDatabaseManager::class.java.simpleName

    fun query(realm: Realm) : RealmResults<DataRealm> {

        var dataListRealmResult: RealmResults<DataRealm>? = null

        realm.executeTransaction{
            dataListRealmResult = realm.where<DataRealm>(DataRealm::class.java).findAll()
        }

        return dataListRealmResult!!
    }

    fun queryFilter(filterStr: String, realm: Realm) : RealmResults<DataRealm> {

        var dataListRealmResult: RealmResults<DataRealm>? = null

        realm.executeTransaction{
            dataListRealmResult = realm.where<DataRealm>(DataRealm::class.java).
            contains("name", filterStr, Case.INSENSITIVE).
            or().
            contains("rating", filterStr, Case.INSENSITIVE).
            or().
            contains("created_at", filterStr, Case.INSENSITIVE).
            findAll()
        }

        return dataListRealmResult!!
    }

    fun insert(dataList : DramaList, realm: Realm) {

        Log.d(TAG, "insert: " + dataList.data.size)

        realm.executeTransaction{

            val dataListRealmResult = realm.where<DataRealm>(DataRealm::class.java).findAll()

            if(dataListRealmResult.isEmpty()) {
                Log.d(TAG, "Need insert data!!!")

                for(data in dataList.data) {
                    val newDataInfo = DataRealm(data.dramaId,
                        data.createdAt,
                        data.name,
                        data.rating.toString(),
                        data.thumb,
                        data.dramaId,
                        "")

                    it.copyToRealmOrUpdate(newDataInfo)
                }

            } else {
                Log.d(TAG, "Not need insert data!!!")
            }
        }
    }

    fun updateBitmap(data : Data, base64Str: String, realm: Realm) {

        realm.executeTransaction{

            val dataRealmResult = realm.where<DataRealm>(DataRealm::class.java)
                .equalTo("drama_id", data.dramaId)
                .findFirst()

            dataRealmResult!!.base64Thumb = base64Str
        }

    }








}