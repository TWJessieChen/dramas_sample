package com.example.dramas_sample.http

import com.example.dramas_sample.data.DramaList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface DramaApi {

    @POST("data")
    suspend fun getDramaList(): Response<DramaList>

}