package com.example.frontendbook.data.api

import com.example.frontendbook.data.api.service.BooksApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: BooksApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApiService::class.java)
    }
}
