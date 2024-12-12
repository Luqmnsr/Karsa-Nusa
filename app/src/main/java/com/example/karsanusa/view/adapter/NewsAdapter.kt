package com.example.karsanusa.view.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.karsanusa.R
import com.example.karsanusa.data.remote.response.NewsResponseItem
import com.example.karsanusa.databinding.ItemNewsBinding

class NewsAdapter(
    private val onClick: (NewsResponseItem) -> Unit
) : ListAdapter<NewsResponseItem, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story, onClick)
        }
    }

    class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NewsResponseItem, onClick: (NewsResponseItem) -> Unit) {
            binding.tvItemTitle.text = news.title
            binding.tvItemSnippet.text = news.snippet

            val imageUrl = news.thumbnail
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.error_placeholder)
                .into(binding.ivItemThumbnail)

            binding.root.setOnClickListener {
                onClick(news)
            }

            binding.tvItemLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.link))
                binding.root.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsResponseItem>() {
            override fun areItemsTheSame(
                oldItem: NewsResponseItem,
                newItem: NewsResponseItem
            ): Boolean {
                return oldItem.position == newItem.position
            }

            override fun areContentsTheSame(
                oldItem: NewsResponseItem,
                newItem: NewsResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
