package com.fehty.moneytracker.Api

import com.fehty.moneytracker.Data.AddItemResult
import com.fehty.moneytracker.Data.AuthResult
import com.fehty.moneytracker.Data.DataList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface Api {

    @GET("auth")
    fun auth(@Query("social_user_id") userId: String): Call<AuthResult>

    @GET("items")
    fun getItems(@Query("type") type: String, @Query("auth-token") token: String): Call<MutableList<DataList>>

    @POST("items/add")
    fun addItem(@Query("price") price: String, @Query("name") name: String, @Query("type") type: String, @Query("auth-token") token: String): Call<AddItemResult>

    @POST("items/remove")
    fun removeItem(@Query("id") id: Int, @Query("auth-token") token: String): Call<AddItemResult>
}

