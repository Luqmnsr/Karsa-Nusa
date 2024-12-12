package com.example.karsanusa.data.remote.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("NewsResponse")
	val newsResponse: List<NewsResponseItem>
)

data class NewsResponseItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("snippet")
	val snippet: String,

	@field:SerializedName("thumbnail")
	val thumbnail: String,

	@field:SerializedName("favicon")
	val favicon: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("position")
	val position: Int,

	@field:SerializedName("source")
	val source: String,

	@field:SerializedName("title")
	val title: String
)
