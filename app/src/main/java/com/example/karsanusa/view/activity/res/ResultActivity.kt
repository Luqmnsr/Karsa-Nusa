package com.example.karsanusa.view.activity.res

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
import com.example.karsanusa.view.adapter.ResultAdapter
import com.example.karsanusa.view.listener.ResultListener

class ResultActivity : AppCompatActivity(), ResultListener {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val extraResponse: ModelResponse?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extraResponse = intent.getParcelableExtra(EXTRA_RESPONSE, ModelResponse::class.java)
        }
        else {
            extraResponse = intent.getParcelableExtra<ModelResponse>(EXTRA_RESPONSE)
        }

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
        // Handle ketika recycler view item diklik di sini. Contoh:
        showToast(item.identifier)

        // Nah, tinggal request ke api detail batik lagi, kan ga ada url item nya dikasih nih.
        // Kalo mau gampang tinggal minta ke cc diubah dikit wkwkk biar tinggal intent uri di sini
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_RESPONSE = "extra_response"
    }
}
