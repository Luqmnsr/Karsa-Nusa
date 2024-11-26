package com.example.karsanusa.view.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.karsanusa.data.preference.ThemePreference
import com.example.karsanusa.data.repository.UserRepository
import com.example.karsanusa.view.ui.profile.ProfileViewModel

class ThemeViewModelFactory(
    private val pref: ThemePreference,
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(pref, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}