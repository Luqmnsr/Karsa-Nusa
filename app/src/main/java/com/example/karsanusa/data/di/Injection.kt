package com.example.karsanusa.data.di

import android.content.Context
import com.example.karsanusa.data.preference.UserPreference
import com.example.karsanusa.data.preference.dataStore
import com.example.karsanusa.data.repository.UserRepository
//import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
//        val user = runBlocking { pref.getSession().first() }
//        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref,
//            apiService
        )
    }

    fun provideUserRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)

        return UserRepository.getInstance(userPreference)
    }
}
