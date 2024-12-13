package com.example.karsanusa.view.activity.batik

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.karsanusa.data.remote.response.DetailResponse
import com.example.karsanusa.data.remote.response.ModelResponse
import com.example.karsanusa.data.repository.ModelRepository
import com.example.karsanusa.data.result.Result
import okhttp3.MultipartBody

class BatikViewModel(
    private val modelRepository: ModelRepository
) : ViewModel() {

    fun predictBatik(
        image: MultipartBody.Part
    ): LiveData<Result<ModelResponse>> {
        return modelRepository.predictBatik(image)
    }

    fun getBatikDetail(batikIdentifier: String) : LiveData<Result<DetailResponse>> =
        modelRepository.getBatikDetails(batikIdentifier)
}
