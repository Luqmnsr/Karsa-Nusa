package com.example.karsanusa.view.vmfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.karsanusa.data.di.Injection
import com.example.karsanusa.data.repository.AuthRepository
import com.example.karsanusa.view.authentication.login.LoginViewModel
import com.example.karsanusa.view.authentication.register.RegisterViewModel

class SessionViewModelFactory(
    private val repository: AuthRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): SessionViewModelFactory {
            if (INSTANCE == null) {
                synchronized(SessionViewModelFactory::class.java) {
                    INSTANCE = SessionViewModelFactory(Injection.provideAuthRepository(context))
                }
            }
            return INSTANCE as SessionViewModelFactory
        }
    }
}