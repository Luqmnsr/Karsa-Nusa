package com.example.karsanusa.view.vmfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.karsanusa.data.di.Injection
import com.example.karsanusa.data.repository.UserRepository
import com.example.karsanusa.view.authentication.register.RegisterViewModel

class RegisterViewModelFactory private constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create (modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: RegisterViewModelFactory? = null

        fun getInstance(context: Context): RegisterViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = RegisterViewModelFactory(Injection.provideUserRepository(context))
                INSTANCE = instance
                instance
            }
        }
    }
}
