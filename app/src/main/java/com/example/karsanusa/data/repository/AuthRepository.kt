package com.example.karsanusa.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
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

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(
        fullname: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiServiceAuth.register(fullname, email, password)
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

    fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiServiceAuth.login(email, password)
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