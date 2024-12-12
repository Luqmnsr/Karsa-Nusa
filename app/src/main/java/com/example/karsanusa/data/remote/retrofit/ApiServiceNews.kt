package com.example.karsanusa.data.remote.retrofit

import com.example.karsanusa.data.remote.response.NewsResponse
import retrofit2.http.GET

interface ApiServiceNews {

    @GET("/")
    suspend fun getNews(
    ): NewsResponse

}