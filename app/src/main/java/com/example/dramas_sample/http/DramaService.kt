package com.example.dramas_sample.http

import com.example.dramas_sample.http.DramaContact.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DramaService {

    fun getDramaService(): DramaApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DramaApi::class.java)
    }


}