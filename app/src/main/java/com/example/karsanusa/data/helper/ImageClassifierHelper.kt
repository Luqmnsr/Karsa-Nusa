//package com.example.karsanusa.data.helper
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.os.SystemClock
//import android.provider.MediaStore
//import android.util.Log
//import com.example.karsanusa.R
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.support.common.ops.CastOp
//import org.tensorflow.lite.support.image.ImageProcessor
//import org.tensorflow.lite.support.image.TensorImage
//import org.tensorflow.lite.support.image.ops.ResizeOp
//import org.tensorflow.lite.task.core.BaseOptions
//import org.tensorflow.lite.task.vision.classifier.Classifications
//import org.tensorflow.lite.task.vision.classifier.ImageClassifier
//
//class ImageClassifierHelper(
//    private var threshold: Float = 0.1f,
//    private var maxResults: Int = 3,
//    private val modelName: String = "cancer_classification.tflite",
//    val context: Context,
//    val classifierListener: ClassifierListener?
//) {
//
//    private var imageClassifier: ImageClassifier? = null
//
//    init {
//        setupImageClassifier()
//    }
//
//    private fun setupImageClassifier() {
//        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
//            .setScoreThreshold(threshold)
//            .setMaxResults(maxResults)
//        val baseOptionsBuilder = BaseOptions.builder()
//            .setNumThreads(4)
//        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())
//
//        try {
//            imageClassifier = ImageClassifier.createFromFileAndOptions(
//                context,
//                modelName,
//                optionsBuilder.build()
//            )
//        } catch (e: IllegalStateException) {
//            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
//            Log.e(TAG, e.message.toString())
//        }
//    }
//
//    fun classifyStaticImage(imageUri: Uri) {
//        if (imageClassifier == null) {
//            setupImageClassifier()
//        }
//
//        val imageProcessor = ImageProcessor.Builder()
//            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
//            .add(CastOp(DataType.FLOAT32))
//            .build()
//
//        try {
//            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
//                ImageDecoder.decodeBitmap(source)
//            } else {
//                @Suppress("DEPRECATION")
//                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
//            }.copy(Bitmap.Config.ARGB_8888, true)
//
//            val startTime = SystemClock.uptimeMillis()
//            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
//            val results = imageClassifier?.classify(tensorImage)
//            val inferenceTime = SystemClock.uptimeMillis() - startTime
//
//            if (results.isNullOrEmpty()) {
//                classifierListener?.onError(context.getString(R.string.image_classifier_failed))
//            } else {
//                classifierListener?.onResults(results, inferenceTime)
//            }
//        } catch (e: Exception) {
//            classifierListener?.onError("Error processing image: ${e.message}")
//            Log.e(TAG, "Error during classification: ${e.message}")
//        }
//    }
//
//    interface ClassifierListener {
//        fun onError(error: String)
//        fun onResults(
//            results: List<Classifications>?,
//            inferenceTime: Long
//        )
//    }
//
//    companion object {
//        private const val TAG = "ImageClassifierHelper"
//    }
//}