package com.example.karsanusa.view.vmfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.karsanusa.data.di.Injection
import com.example.karsanusa.data.repository.NewsRepository
import com.example.karsanusa.data.repository.AuthRepository
import com.example.karsanusa.view.ui.home.HomeViewModel

class HomeViewModelFactory private constructor(
    private val newsRepository: NewsRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(newsRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: HomeViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): HomeViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeViewModelFactory(
                    Injection.provideNewsRepository(context),
                    Injection.provideAuthRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}

