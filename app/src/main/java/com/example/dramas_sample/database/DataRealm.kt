package com.example.dramas_sample.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DataRealm (
//    @field:PrimaryKey
    var drama_id: Int? = 0,
    var created_at: String? = "",
    var name: String? = "",
    var rating: Double? = 0.0,
    var thumb: String? = "",
    var total_views: Int? = 0
) : RealmObject() {

}