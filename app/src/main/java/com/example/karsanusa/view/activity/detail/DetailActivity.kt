package com.example.karsanusa.view.activity.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.karsanusa.R
import com.example.karsanusa.data.result.Result
import com.example.karsanusa.databinding.ActivityDetailBinding
import com.example.karsanusa.view.activity.batik.BatikViewModel
import com.example.karsanusa.view.vmfactory.BatikViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<BatikViewModel> {
        BatikViewModelFactory.getInstance(this)
    }

    companion object {
        const val EXTRA_IDENTIFIER = "extra_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val url = intent.getStringExtra(EXTRA_IDENTIFIER)

        showLoading(true)

        if (url != null) {
            viewModel.getBatikDetail(url).observe(this) { response ->
                showLoading(false)
                when (response) {
                    is Result.Success -> {
                        Log.d("BatikActivity", "Detection successful: ${response.data}")
                        binding.resultTextScan.text = response.data.detailResponse.name
                        binding.detailLocation.text = response.data.detailResponse.location
                        binding.detailDescription.text = response.data.detailResponse.description
                        Glide.with(this@DetailActivity)
                            .load(response.data.detailResponse.imageUrl)
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.error_placeholder)
                            .into(binding.resultSnapshot)
                    }

                    is Result.Loading -> {
                        Log.d("DetailActivtiy", "Loading...")
                        showLoading(true)
                    }

                    is Result.Error -> {
                        Log.e("DetailActivity", "Error: ${response.error}")
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
