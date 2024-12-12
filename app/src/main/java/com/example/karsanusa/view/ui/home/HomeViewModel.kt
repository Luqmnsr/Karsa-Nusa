package com.example.karsanusa.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.karsanusa.data.preference.UserModel
import com.example.karsanusa.data.remote.response.BatikResponseItem
import com.example.karsanusa.data.repository.NewsRepository
import com.example.karsanusa.data.repository.AuthRepository
import com.example.karsanusa.data.result.Result

class HomeViewModel(
    private val newsRepository: NewsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getNews(): LiveData<Result<List<BatikResponseItem>>> {
        return newsRepository.getNews()
    }

    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }
}
