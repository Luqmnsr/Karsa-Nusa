package com.example.karsanusa.view.vmfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.karsanusa.data.di.Injection
import com.example.karsanusa.data.preference.ThemePreference
import com.example.karsanusa.data.repository.AuthRepository
import com.example.karsanusa.view.ui.profile.ProfileViewModel

class ThemeViewModelFactory(
    private val pref: ThemePreference,
    private val repository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(pref, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
    companion object {
        @Volatile
        private var INSTANCE: ThemeViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ThemeViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ThemeViewModelFactory(
                    Injection.provideThemePreference(context),
                    Injection.provideAuthRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}