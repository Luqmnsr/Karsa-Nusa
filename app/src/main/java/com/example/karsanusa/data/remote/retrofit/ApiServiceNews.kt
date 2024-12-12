package com.example.karsanusa.data.remote.retrofit

import com.example.karsanusa.data.remote.response.NewsResponseItem
import retrofit2.http.GET

interface ApiServiceNews {

    @GET("/")
    suspend fun getNews(
    ): ArrayList<NewsResponseItem>

}