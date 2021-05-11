package com.example.dramas_sample.data

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("drama_id")
    val dramaId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("thumb")
    val thumb: String,
    @SerializedName("total_views")
    val totalViews: Int
)