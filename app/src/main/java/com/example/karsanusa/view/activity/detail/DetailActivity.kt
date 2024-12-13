package com.example.karsanusa.view.activity.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

        if (url != null) {
            viewModel.getBatikDetail(url).observe(this) { response ->
                when (response) {
                    is Result.Success -> {
                        binding.detailName.text = response.data.detailResponse.name
                        binding.detailLocation.text = response.data.detailResponse.location
                        binding.detailDescription.text = response.data.detailResponse.description
                        Glide.with(this@DetailActivity)
                            .load(response.data.detailResponse.imageUrl)
                            .into(binding.detailImage)
                    }

                    is Result.Loading -> {

                    }

                    is Result.Error -> {

                    }
                }
            }
        }
    }
}
