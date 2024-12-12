package com.example.karsanusa.view.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karsanusa.data.remote.response.LoginResponse
import com.example.karsanusa.data.repository.AuthRepository
import kotlinx.coroutines.launch
import com.example.karsanusa.data.result.Result

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> {
        return authRepository.login(email, password)
    }

    fun saveSession(email: String, token: String) {
        viewModelScope.launch {
            authRepository.saveSession(email, token)
        }
    }
}
