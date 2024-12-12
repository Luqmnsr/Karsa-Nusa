package com.example.karsanusa.data.remote.response

import com.google.gson.annotations.SerializedName

data class ModelResponse(

	@field:SerializedName("predictions")
	val predictions: Map<String, Double> // Using a Map for batik name and confidence
)
