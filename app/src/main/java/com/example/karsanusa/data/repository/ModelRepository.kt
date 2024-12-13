package com.example.karsanusa.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.karsanusa.data.remote.response.DetailResponse
import com.example.karsanusa.data.remote.response.ErrorResponse
import com.example.karsanusa.data.remote.response.ModelResponse
import com.example.karsanusa.data.remote.retrofit.ApiServiceModel
import com.example.karsanusa.data.result.Result
import com.google.gson.Gson
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException

class ModelRepository private constructor(
    private val apiServiceModel: ApiServiceModel
) {

    fun predictBatik(
        image: MultipartBody.Part
    ): LiveData<Result<ModelResponse>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiServiceModel.predictBatik(image)
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

    fun getBatikDetails(
        batikIdentifier: String
    ): LiveData<Result<DetailResponse>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiServiceModel.getBatikDetails(batikIdentifier)
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
        private var instance: ModelRepository? = null
        fun getInstance(
            apiServiceModel: ApiServiceModel
        ): ModelRepository =
            instance ?: synchronized(this) {
                instance ?: ModelRepository(apiServiceModel)
            }.also { instance = it }
    }
}
