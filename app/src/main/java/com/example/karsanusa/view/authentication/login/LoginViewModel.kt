package com.example.karsanusa.view.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karsanusa.data.preference.UserModel
import com.example.karsanusa.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            authRepository.saveSession(user)
        }
    }
    fun login(
        email: String,
        password: String) = authRepository.login(email,password)
}