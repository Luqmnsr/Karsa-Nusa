package com.example.karsanusa.data.remote.retrofit

import com.example.karsanusa.data.remote.response.DetailModelResponse
import com.example.karsanusa.data.remote.response.ModelResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiServiceModel {
    @Multipart
    @POST("predict")
    suspend fun predictBatik(
        @Part image: MultipartBody.Part
    ): ModelResponse

    @GET("details/{batikIdentifier}")
    suspend fun getBatikDetails(
        @Path("batikIdentifier") batikIdentifier: String
    ): DetailModelResponse
}