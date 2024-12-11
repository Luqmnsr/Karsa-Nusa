package com.example.karsanusa.view.activity.batik

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.karsanusa.BuildConfig
import com.yalantis.ucrop.UCrop
import com.example.karsanusa.R
import com.example.karsanusa.databinding.ActivityBatikBinding
import com.example.karsanusa.view.activity.main.MainActivity
import com.example.karsanusa.view.activity.res.ResultActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class BatikActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBatikBinding
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatikBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            requestPermissionLauncher.launch(permission)
        }

        setupActionBar()
        setupAction()
        setupBackPressedCallback()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupAction() {
        binding.galeriButton.setOnClickListener {
            galleryLaunch.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.cameraButton.setOnClickListener {
            currentImageUri = getImageUri(this)
            cameraLaunch.launch(currentImageUri!!)
        }

        binding.analyzeButton.setOnClickListener {
            moveToResult()
        }
    }

    private fun setupBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackPressedDialog()
            }
        })
    }

    private fun showBackPressedDialog() {
        AlertDialog.Builder(this@BatikActivity).apply {
            setMessage(R.string.analyze_confirmation)
            setPositiveButton(R.string.done) { _, _ ->
                navigateToMainActivity()
            }
            setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun navigateToMainActivity() {
        runBlocking {
            delay(500)
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private val galleryLaunch = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            currentImageUri = uri
            startCrop(uri)
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val cameraLaunch = registerForActivityResult(
        ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let { startCrop(it) }
            showImage()
        } else {
            currentImageUri = null
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
        if (currentImageUri != null) {
            binding.previewImageView.setImageURI(currentImageUri)
            binding.analyzeButton.visibility = View.VISIBLE
        } else {
            binding.previewImageView.setImageResource(R.drawable.img_placeholder) // Set placeholder
            binding.analyzeButton.visibility = View.GONE
        }
    }

//    private fun analyzeImage() {
//        if (currentImageUri != null) {
//            binding.progressIndicator.visibility = View.VISIBLE
////            imageClassifierHelper.classifyStaticImage(currentImageUri!!)
//        } else {
//            showToast(getString(R.string.image_classifier_failed))
//            Log.e("AnalyzeImage", "Image URI is null.")
//        }
//    }

//    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
//        binding.progressIndicator.visibility = View.GONE
//        results?.let {
//            // Filter hasil klasifikasi untuk hanya kategori dengan skor > 50%
//            val filteredResultText = it.joinToString("\n") { classification ->
//                classification.categories
//                    .filter { category -> category.score > 0.5 }  // Menyaring hanya kategori dengan skor > 50%
//                    .joinToString { category -> "${category.label}: ${category.score.times(100).toInt()}%" }
//            }.trim()  // Menghapus baris kosong yang mungkin muncul jika tidak ada kategori di atas 50%
//
//            // Log hasil yang difilter
//            Log.d("Filtered Classification Result", filteredResultText)
//
//            // Menampilkan pesan berdasarkan hasil yang difilter
//            if (filteredResultText.isNotEmpty()) {
//                // Mencari kategori dengan skor tertinggi di antara hasil yang difilter
//                val highestCategory = it.flatMap { classification ->
//                    classification.categories
//                }.maxByOrNull { category -> category.score }
//
//                highestCategory?.let { category ->
//                    if (category.score > 0.5) {
//                        if (category.label.equals("Cancer", ignoreCase = true)) {
//                            val message = "Gambar tersebut merupakan contoh kanker kulit."
//                            showToast(message)
//                            moveToResult("$filteredResultText\n\n$message")
//                        } else if (category.label.trim().equals("Non Cancer", ignoreCase = true)) {
//                            val message = "Gambar tersbut bukan merupakan contoh kanker kulit."
//                            showToast(message)
//                            moveToResult("$filteredResultText\n\n$message")
//                        } else {
//                            // Jika label tidak sesuai dengan "cancer" atau "non cancer", tampilkan pesan ketidakpastian
//                            val message = "Gambar tidak dikenali, hasil tidak dapat dipastikan."
//                            showToast(message)
//                            moveToResult("$filteredResultText\n\n$message")
//                        }
//                    } else {
//                        // Jika skor kategori tertinggi kurang dari 50%
//                        val message = "Tidak ada kategori dengan persentase > 50% yang terdeteksi."
//                        showToast(message)
//                        moveToResult("$filteredResultText\n\n$message")
//                    }
//                }
//            }
//        } ?: showToast("No classification results")
//    }
//
//    override fun onError(error: String) {
//        binding.progressIndicator.visibility = View.GONE
//        showToast(error)
//    }

    private fun moveToResult(result: String = "Hasil Default") {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
            putExtra(ResultActivity.EXTRA_RESULT, result)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateToMainActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}