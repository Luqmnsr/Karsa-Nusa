package com.example.karsanusa.view.activity.res

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.karsanusa.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }
        val result = intent.getStringExtra(EXTRA_RESULT)

        // Tampilkan gambar
        if (imageUri != null) {
            Log.d("ResultActivity", "Image URI: $imageUri")
            binding.resultSnapshot.setImageURI(imageUri)
        } else {
            Log.e("ResultActivity", "Image URI is null")
            showToast("Failed to load image")
        }

        // Tampilkan hasil deteksi
        if (result != null) {
            Log.d("ResultActivity", "Detection Result: $result")
            binding.resultText.text = result
        } else {
            Log.e("ResultActivity", "Result text is null")
            showToast("No detection result available")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}