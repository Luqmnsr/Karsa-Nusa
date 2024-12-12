package com.example.karsanusa.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("fullName")
	val fullName: String,

	@field:SerializedName("email")
	val email: String
)
