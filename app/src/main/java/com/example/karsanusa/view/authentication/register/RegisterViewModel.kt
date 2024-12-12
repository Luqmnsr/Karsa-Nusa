package com.example.karsanusa.view.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.karsanusa.data.remote.response.RegisterResponse
import com.example.karsanusa.data.repository.AuthRepository
import com.example.karsanusa.data.result.Result

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun register(
        email: String,
        password: String,
        fullname: String
    ): LiveData<Result<RegisterResponse>> {
        return authRepository.register(email, password, fullname)
    }
}
