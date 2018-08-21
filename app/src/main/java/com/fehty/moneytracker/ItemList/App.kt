package com.fehty.moneytracker.ItemList

import android.app.Application
import android.text.TextUtils
import com.fehty.moneytracker.Api.Api
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    private var api: Api? = null
    private var baseUrl = "http://android.loftschool.com/basic/v1/"
    private val PREFS_NAME = "shared_prefs"
    private val KEY_TOKEN = "auth_token"

    override fun onCreate() {
        super.onCreate()
        val interceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        val json = GsonBuilder()
                .setDateFormat("dd.MM.yyyy HH:mm:ss")
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(json))
                .client(client)
                .build()
        api = retrofit.create(Api::class.java)
    }

    fun getApi(): Api? {
        return api
    }

    fun saveAuthToken(token: String) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_TOKEN, token)
                .apply()
    }

    fun getAuthToken(): String? {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(KEY_TOKEN, null)
    }

    fun isAuthorized(): Boolean {
        return !TextUtils.isEmpty(getAuthToken())
    }

}
