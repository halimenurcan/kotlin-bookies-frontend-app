package com.example.frontendbook.data.remote

import com.example.frontendbook.data.api.BooksApiService
import com.example.frontendbook.data.api.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_BOOKS_URL = "https://www.googleapis.com/books/v1/"
    private const val BASE_USER_URL = "https://localhost:8080/api/"

    val booksApiService: BooksApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_BOOKS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApiService::class.java)
    }

    val userApiService: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_USER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }
}
