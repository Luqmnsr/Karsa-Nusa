package com.example.karsanusa.data.remote.response

import com.google.gson.annotations.SerializedName

data class ModelResponse(

	@field:SerializedName("listPredictions")
	val listPredictions: List<ListPredictionsItem>
)

data class ListPredictionsItem(

	@field:SerializedName("identifier")
	val identifier: String,

	@field:SerializedName("confidence")
	val confidence: Any,

	@field:SerializedName("name")
	val name: String
)
