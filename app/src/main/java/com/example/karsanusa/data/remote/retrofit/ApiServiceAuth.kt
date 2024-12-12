package com.example.karsanusa.data.remote.retrofit

import com.example.karsanusa.data.preference.LoginRequest
import com.example.karsanusa.data.preference.RegisterRequest
import com.example.karsanusa.data.remote.response.LoginResponse
import com.example.karsanusa.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceAuth {
    @POST("api/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}