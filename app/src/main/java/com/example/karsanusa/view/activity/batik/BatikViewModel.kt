package com.example.karsanusa.view.activity.batik

import android.net.Uri
import androidx.lifecycle.ViewModel

class BatikViewModel : ViewModel() {
    private var currentImageUri: Uri? = null

    fun getCurrentImageUri() = currentImageUri

    fun setCurrentImageUri(uri: Uri?) {
        currentImageUri = uri
    }
}
