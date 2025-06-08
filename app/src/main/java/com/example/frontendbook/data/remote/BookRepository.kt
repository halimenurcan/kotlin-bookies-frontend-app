package com.example.frontendbook.data.remote

import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.retrofit.ApiService
import com.example.frontendbook.ui.search.SortOption
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-backend-url.com/") // 🔁 ← buraya gerçek backend base URL’ini yaz
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    suspend fun fetchSortedBooks(option: SortOption): List<Book> {
        val response = api.getSortedBooks(option.name.lowercase())
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("API error: ${response.code()} ${response.message()}")
        }
    }

    suspend fun searchBooks(query: String): List<Book> {
        val response = api.searchBooks(query)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("API error: ${response.code()} ${response.message()}")
        }
    }

    // İleride: genre, country, language filtreleme fonksiyonları da buraya eklenecek
}
