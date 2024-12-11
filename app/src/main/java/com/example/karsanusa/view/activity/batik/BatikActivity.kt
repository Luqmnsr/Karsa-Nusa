package com.example.karsanusa.view.activity.batik

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.karsanusa.BuildConfig
import com.example.karsanusa.databinding.ActivityBatikBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BatikActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBatikBinding
    private lateinit var viewModel: BatikViewModel
    private val intentGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { result ->
        if (result != null) {
            startCrop(result)
        }
        else {
            makeToast("Tidak dapat menemukan gambar")
        }
    }
    private val intentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            val currentImage = viewModel.getCurrentImageUri()

            if (currentImage != null) {
                startCrop(currentImage)
            }
            else {
                makeToast("Tidak dapat menemukan gambar")
            }
        }
        else {
            makeToast("Tidak dapat menemukan gambar")
        }
    }

    // Fungsi seganteng ini di-deprecated sama seorang Google. Google selingkuh bucin sama Kotlin.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
            val destinationImageUri = UCrop.getOutput(data)

            viewModel.setCurrentImageUri(destinationImageUri)
            startShowImage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBatikBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[BatikViewModel::class.java]

        setContentView(binding.root)

        startShowImage()
        startListener()
    }

    private fun startShowImage() {
        val imageUri = viewModel.getCurrentImageUri()

        if (imageUri != null) {
            binding.previewImageView.setImageURI(imageUri)
        }
    }

    private fun startListener() {
        binding.buttonIntentGallery.setOnClickListener { _ ->
            startGallery()
        }

        binding.buttonIntentCamera.setOnClickListener { _ ->
            startCamera()
        }
    }

    private fun startGallery() {
        val pickType = ActivityResultContracts.PickVisualMedia.ImageOnly

        intentGallery.launch(PickVisualMediaRequest(pickType))
    }

    private fun startCamera() {
        val fileNameFormat = "yyyyMMdd_HHmmss"
        val timeStamp = SimpleDateFormat(fileNameFormat, Locale.US).format(Date())
        var imageUri: Uri? = null

        // Past Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues()

            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/KarsaNusa")

            imageUri = applicationContext.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        }
        // Pre Q
        else {
            val filesDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File(filesDir, "/KarsaNusa/$timeStamp.jpg")

            if (imageFile.parentFile?.exists() == false) {
                imageFile.parentFile?.mkdir()
            }

            imageUri = FileProvider.getUriForFile(
                applicationContext,
                "${BuildConfig.APPLICATION_ID}.fileprovider",
                imageFile
            )
        }

        if (imageUri != null) {
            viewModel.setCurrentImageUri(imageUri)
            intentCamera.launch(imageUri)
        }
        else {
            makeToast("Tidak dapat menulis berkas gambar ke direktori")
        }
    }

    private fun startCrop(sourceImageUri: Uri) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val fileName = "IMG_${hour}_${minute}_${second}"
        val file = File.createTempFile(fileName, ".jpg", applicationContext.cacheDir)
        val imageDestinationUri: Uri = Uri.fromFile(file)

        UCrop.of(sourceImageUri, imageDestinationUri)
            .withAspectRatio(1F, 1F)
            .withMaxResultSize(224, 224)
            .start(this)

        // After this function is called, onActivityResult will be called back.
    }

    private fun makeToast(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT
        )
            .show()
    }
}
