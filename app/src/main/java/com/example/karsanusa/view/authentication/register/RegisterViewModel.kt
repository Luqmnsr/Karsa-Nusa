package com.example.karsanusa.view.authentication.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.karsanusa.data.preference.UserModel
import com.example.karsanusa.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun saveSession(userModel: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(userModel)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}
