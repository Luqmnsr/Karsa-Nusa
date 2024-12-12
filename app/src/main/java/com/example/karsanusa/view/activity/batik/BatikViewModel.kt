package com.example.karsanusa.view.activity.batik

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.karsanusa.data.remote.response.ListPredictionsItem
import com.example.karsanusa.data.repository.ModelRepository
import com.example.karsanusa.data.result.Result
import okhttp3.MultipartBody

class BatikViewModel(
    private val modelRepository: ModelRepository
) : ViewModel() {

    fun predictBatik(
        image: MultipartBody.Part
    ): LiveData<Result<List<ListPredictionsItem>>> {
        return modelRepository.predictBatik(image)
    }
}
