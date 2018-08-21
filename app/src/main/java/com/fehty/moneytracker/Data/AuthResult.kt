package com.fehty.moneytracker.Data
import com.google.gson.annotations.SerializedName



data class AuthResult(
        val status: String,
		val id: Int,
		@SerializedName("auth_token")
        val token: String
)