package com.example.karsanusa.data.di

import android.content.Context
import com.example.karsanusa.data.preference.UserPreference
import com.example.karsanusa.data.preference.dataStore
import com.example.karsanusa.data.remote.retrofit.ApiConfig
import com.example.karsanusa.data.repository.NewsRepository
import com.example.karsanusa.data.repository.UserRepository


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideNewsRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }
}
