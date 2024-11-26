package com.example.karsanusa.view.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.karsanusa.data.preference.ThemePreference
import com.example.karsanusa.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val pref: ThemePreference,
    private val repository: UserRepository
) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
