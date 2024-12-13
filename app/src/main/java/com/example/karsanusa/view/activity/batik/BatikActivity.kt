package com.example.karsanusa.view.activity.batik

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.karsanusa.R
import com.example.karsanusa.data.remote.response.ModelResponse
import com.example.karsanusa.data.result.Result
import com.example.karsanusa.databinding.ActivityBatikBinding
import com.example.karsanusa.view.activity.res.ResultActivity
import com.example.karsanusa.view.vmfactory.BatikViewModelFactory
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class BatikActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBatikBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<BatikViewModel> {
        BatikViewModelFactory.getInstance(this)
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { (permission, isGranted) ->
                if (!isGranted) {
                    Toast.makeText(this, "Permission $permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun allPermissionsGranted(): Boolean {
        return requiredPermissions.all { permission -> // Use requiredPermissions
            ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val requiredPermissions = arrayOf( // Renamed to requiredPermissions
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatikBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(requiredPermissions) // Use requiredPermissions
        }

        binding.buttonIntentGallery.setOnClickListener { startGallery() }
        binding.buttonIntentCamera.setOnClickListener { startCamera() }
        binding.buttonIntentAnalyze.setOnClickListener { startAnalyze() }

        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            startCrop(uri)
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        val imageUri = getImageUri(this)
        currentImageUri = imageUri
        launcherIntentCamera.launch(imageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            val uri = currentImageUri
            uri?.let {
                startCrop(it)
                showImage()
            }
        } else {
            Log.e("Photo Camera", "Failed")
        }
    }

    private fun startCrop(imageUri: Uri) {
        val uniqueFileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(this.cacheDir, uniqueFileName))

        val uCropIntent = UCrop.of(imageUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(224, 224)
            .getIntent(this)

        cropImageResultLauncher.launch(uCropIntent)
    }

    private val cropImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let {
                currentImageUri = it
                showImage()
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            cropError?.let {
                showToast(getString(R.string.image_load_failed))
            } ?: showToast("Error cropping image.")
        } else {
            // Jika proses cropping dibatalkan
            showToast("Cropping canceled.")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startAnalyze() {
        val imageUri = currentImageUri // Create a local immutable copy
        if (imageUri == null) {
            showToast("No image selected")
            return
        }

        val imageFile = uriToFile(imageUri, this).reduceFileImage()
        Log.d("Image Classification File", "showImage: ${imageFile.path}")

        showLoading(true)

        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("image", imageFile.name, requestImageFile)

        viewModel.predictBatik(multipartBody).observe(this) { response ->
            showLoading(false)
            when (response) {
                is Result.Error -> {
                    Log.e("BatikActivity", "Detection failed: ${response.error}")
                    showToast(getString(R.string.failed_detection))
                }
                Result.Loading -> {
                    Log.d("BatikActivity", "Loading detection...")
                    showLoading(true)
                }
                is Result.Success -> {
                    Log.d("BatikActivity", "Detection successful: ${response.data}")
                    moveToResult(response.data)
                    showToast(getString(R.string.success_detection))
                }
            }
        }
    }

    private fun moveToResult(extraResponse: ModelResponse) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_RESPONSE, extraResponse)
        }
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        showLoading(false)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
