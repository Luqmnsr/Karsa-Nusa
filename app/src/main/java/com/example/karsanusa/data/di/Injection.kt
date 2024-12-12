package com.example.karsanusa.data.di

import android.content.Context
import com.example.karsanusa.data.preference.ThemePreference
import com.example.karsanusa.data.preference.UserPreference
import com.example.karsanusa.data.preference.dataStore
import com.example.karsanusa.data.preference.themeDataStore
import com.example.karsanusa.data.remote.retrofit.ApiConfig
import com.example.karsanusa.data.repository.NewsRepository
import com.example.karsanusa.data.repository.AuthRepository
import com.example.karsanusa.data.repository.ModelRepository


object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiServiceAuth(context)
        return AuthRepository.getInstance(pref,apiService)
    }

    fun provideThemePreference(context: Context): ThemePreference {
        return ThemePreference.getInstance(context.themeDataStore)
    }

    fun provideNewsRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiServiceNews()
        return NewsRepository.getInstance(apiService)
    }

    fun provideModelRepository(context: Context): ModelRepository {
        val apiService = ApiConfig.getApiServiceModel()
        return ModelRepository.getInstance(apiService)
    }
}
