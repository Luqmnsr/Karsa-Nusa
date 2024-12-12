package com.example.karsanusa.view.authentication.register

import androidx.lifecycle.ViewModel
import com.example.karsanusa.data.repository.AuthRepository

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun register(
        fullname: String,
        email: String,
        password: String
    ) = authRepository.register(fullname, email, password)
}
