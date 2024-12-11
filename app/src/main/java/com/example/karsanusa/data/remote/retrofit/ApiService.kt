package com.example.karsanusa.data.remote.retrofit

import com.example.karsanusa.data.remote.response.BatikResponse
import retrofit2.http.GET

interface ApiService {

    @GET("/")
    suspend fun getNews(
    ): BatikResponse

}