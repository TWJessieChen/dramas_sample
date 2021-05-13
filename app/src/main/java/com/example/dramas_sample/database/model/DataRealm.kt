package com.example.dramas_sample.database.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 *
 * 使用Realm 設計database需儲存那些資訊
 *
 * @author JC666
 */


open class DataRealm (
    @field:PrimaryKey
    var drama_id: Int? = 0,
    var created_at: String? = "",
    var name: String? = "",
    var rating: String? = "0.0",
    var thumb: String? = "",
    var total_views: Int? = 0,
    var base64Thumb: String? = ""
) : RealmObject() {

    fun copy(
        drama_id: Int = this.drama_id!!,
        created_at: String = this.created_at!!,
        name: String = this.name!!,
        rating: String = this.rating!!,
        thumb: String = this.thumb!!,
        total_views: Int = this.total_views!!,
        base64Thumb: String = this.base64Thumb!!
        ) = DataRealm(drama_id, created_at, name, rating, thumb, total_views, base64Thumb)
}