package com.example.karsanusa.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("password")
	val password: String,
)
