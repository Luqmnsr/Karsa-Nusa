package com.example.karsanusa.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailModelResponse(

	@field:SerializedName("detailResponse")
	val detailResponse: DetailResponse
)

data class DetailResponse(

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("location")
	val location: String
)
