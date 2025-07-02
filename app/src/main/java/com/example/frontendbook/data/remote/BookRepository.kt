package com.example.frontendbook.data.remote

import android.content.Context
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.retrofit.ApiService
import com.example.frontendbook.ui.search.SortOption
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookRepository(private val context: Context) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // ✅ Buraya gerçek backend URL'ini yaz
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


}
