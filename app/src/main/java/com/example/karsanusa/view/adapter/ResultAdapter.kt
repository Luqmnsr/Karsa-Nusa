package com.example.karsanusa.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.karsanusa.data.remote.response.ListPredictionsItem
import com.example.karsanusa.databinding.ItemPredictionItemBinding
import com.example.karsanusa.view.listener.ResultListener

class ResultAdapter(
    private var resultListener: ResultListener
) : ListAdapter<ListPredictionsItem, ResultAdapter.ResultViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListPredictionsItem> =
            object : DiffUtil.ItemCallback<ListPredictionsItem>() {
                override fun areItemsTheSame(
                    oldItem: ListPredictionsItem,
                    newItem: ListPredictionsItem
                ): Boolean {
                    return oldItem.identifier == newItem.identifier
                }

                override fun areContentsTheSame(
                    oldItem: ListPredictionsItem,
                    newItem: ListPredictionsItem
                ): Boolean {
                    return oldItem.equals(newItem)
                }
            }
    }

    class ResultViewHolder(
        val binding: ItemPredictionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(predictionItem: ListPredictionsItem) {
            binding.predictionItemName.text = predictionItem.name
            binding.predictionItemConfidence.text = predictionItem.confidence.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemPredictionItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val predictionItem = getItem(position)

        holder.bind(predictionItem)
        holder.binding.root.setOnClickListener { v ->
            resultListener.onItemClick(v, predictionItem)
        }
    }
}
