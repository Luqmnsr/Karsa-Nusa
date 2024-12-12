package com.example.karsanusa.view.vmfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.karsanusa.data.di.Injection
import com.example.karsanusa.data.repository.ModelRepository
import com.example.karsanusa.view.activity.batik.BatikViewModel

class BatikViewModelFactory(
    private val modelRepository: ModelRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BatikViewModel::class.java) -> {
                BatikViewModel(modelRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: BatikViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): BatikViewModelFactory {
            if (INSTANCE == null) {
                synchronized(BatikViewModelFactory::class.java) {
                    INSTANCE = BatikViewModelFactory(Injection.provideModelRepository(context))
                }
            }
            return INSTANCE as BatikViewModelFactory
        }
    }
}