package com.example.frontendbook.data.repository

import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.domain.mapper.BookMapper
import com.example.frontendbook.domain.model.Book

import com.example.frontendbook.domain.model.googleapi.BookItem

class BookRepository {

    suspend fun searchBooks(query: String): List<Book> {
        val response = RetrofitClient.booksApiService.searchBooks(query)
        val items = response.items ?: emptyList()
        println("📦 Raw API Items: ${response.items?.map { it.volumeInfo?.title }}")  // DEBUG

        return items.mapNotNull { BookMapper.fromApi(it) }
    }
}
