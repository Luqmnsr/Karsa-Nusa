package com.example.karsanusa.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.karsanusa.data.preference.LoginRequest
import com.example.karsanusa.data.preference.RegisterRequest
import com.example.karsanusa.data.preference.UserModel
import com.example.karsanusa.data.preference.UserPreference
import com.example.karsanusa.data.remote.response.ErrorResponse
import com.example.karsanusa.data.remote.response.LoginResponse
import com.example.karsanusa.data.remote.response.RegisterResponse
import com.example.karsanusa.data.remote.retrofit.ApiServiceAuth
import com.example.karsanusa.data.result.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class AuthRepository private constructor(
    private val userPreference: UserPreference,
    private val apiServiceAuth: ApiServiceAuth
) {

    suspend fun saveSession(email: String, token: String) {
        userPreference.saveSession(email, token)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(
        email: String,
        password: String,
        fullname: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val registerRequest = RegisterRequest(email, password, fullname)
            println("Sending RegisterRequest: $registerRequest") // Log untuk debugging
            val response = apiServiceAuth.register(registerRequest)
            emit(Result.Success(response))
        } catch (e: IOException) {
            emit(Result.Error("No internet connection"))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            println("Error body: $jsonInString") // Log untuk debugging
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody?.message ?: "Unknown server error"
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error("Something went wrong: ${e.message}"))
        }
    }


    fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)

        try {
            val loginRequest = LoginRequest(email, password)
            val response = apiServiceAuth.login(loginRequest)
            saveSession(email, response.token)
            emit(Result.Success(response))
        } catch (e: IOException) {
            emit(Result.Error("No internet connection"))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        } catch (e: Exception) {
            emit(Result.Error("Something went wrong: ${e.message}"))
        }
    }


    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiServiceAuth: ApiServiceAuth
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(userPreference,apiServiceAuth)
            }.also { instance = it }
    }
}