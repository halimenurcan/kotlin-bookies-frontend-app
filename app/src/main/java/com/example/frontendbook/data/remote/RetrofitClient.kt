package com.example.frontendbook.data.remote

import android.content.Context
import com.example.frontendbook.data.api.BooksApiService
import com.example.frontendbook.data.api.UserApiService
import com.example.frontendbook.data.api.ReadApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BOOKS_BASE_URL = "https://www.googleapis.com/books/v1/"
    private const val USER_BASE_URL = "http://10.0.2.2:8080/" // localhost backend için

    private fun getAuthInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val token = prefs.getString("jwt_token", null)

            val requestBuilder = chain.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }
    }

    fun userApiService(context: Context): UserApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    fun readApiService(context: Context): ReadApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor(context)) // ⬅️ Burası çok önemli!
            .build()

        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReadApiService::class.java)
    }

    val booksApiService: BooksApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BOOKS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApiService::class.java)
    }
}
