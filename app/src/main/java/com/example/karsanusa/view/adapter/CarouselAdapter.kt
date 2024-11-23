package com.example.karsanusa.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.karsanusa.data.local.entity.CarouselEntity
import com.example.karsanusa.databinding.ItemCarouselBinding

class CarouselAdapter : ListAdapter<CarouselEntity, CarouselAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<CarouselEntity> = object : DiffUtil.ItemCallback<CarouselEntity>() {
            override fun areItemsTheSame(oldItem: CarouselEntity, newItem: CarouselEntity): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(oldItem: CarouselEntity, newItem: CarouselEntity): Boolean {
                return oldItem.image == newItem.image
            }

        }
    }

    class ViewHolder(val binding: ItemCarouselBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entity: CarouselEntity) {
            binding.itemCarouselImageView.setImageResource(entity.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
        holder.binding.root.setOnClickListener {
            Toast.makeText(holder.binding.root.context, "Item clicked ${holder.adapterPosition}", Toast.LENGTH_SHORT).show()
        }
    }
}