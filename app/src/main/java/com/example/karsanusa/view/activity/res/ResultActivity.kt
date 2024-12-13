package com.example.karsanusa.view.activity.res

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.karsanusa.data.remote.response.ListPredictionsItem
import com.example.karsanusa.data.remote.response.ModelResponse
import com.example.karsanusa.databinding.ActivityResultBinding
import com.example.karsanusa.view.activity.detail.DetailActivity
import com.example.karsanusa.view.adapter.ResultAdapter
import com.example.karsanusa.view.listener.ResultListener

class ResultActivity : AppCompatActivity(), ResultListener {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val extraImageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val extraResponse: ModelResponse?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extraResponse = intent.getParcelableExtra(EXTRA_RESPONSE, ModelResponse::class.java)
        }
        else {
            extraResponse = intent.getParcelableExtra<ModelResponse>(EXTRA_RESPONSE)
        }

        binding.resultSnapshot.setImageURI(Uri.parse(extraImageUri))

        val adapter = ResultAdapter(this)
        val recyclerView = binding.resultRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Submit dengan urutan paramter confidence yang terbesar ke terkecil
        if (extraResponse != null) {
            adapter.submitList(extraResponse.listPredictions.sortedByDescending { it.confidence })
        }
        else {
            Log.d("ResultActivity", "${EXTRA_RESPONSE} is null")
        }
    }

    override fun onItemClick(view: View, item: ListPredictionsItem) {
        val identifier = item.identifier
        val intent = Intent(this, DetailActivity::class.java)

        intent.putExtra(DetailActivity.EXTRA_IDENTIFIER, identifier)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_RESPONSE = "extra_response"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}
