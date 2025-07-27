package com.example.frontendbook.data.repository

import android.content.Context
import android.util.Log
import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.data.api.dto.toDomain
import com.example.frontendbook.data.api.mapper.BookMapper
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.domain.model.Book

class BookRepository(context: Context) {
    private val api = RetrofitClient.booksApiService(context)
    val apiService = RetrofitClient.booksApiService(context)


    suspend fun fetchAllBooks(): List<Book> {
        val resp = api.getAllBooks()
        return if (resp.isSuccessful) {
            resp.body()
                ?.embedded
                ?.books
                .orEmpty()
                .map { BookMapper.fromDto(it) }
        } else {
            val err = resp.errorBody()?.string()
            Log.e("BookRepo", "fetchAllBooks failed: code=${resp.code()}, body=$err")
            throw Exception("Books could not be loaded: ${resp.code()}")
        }
    }

    suspend fun fetchBookById(id: Long): Book {
        val resp = api.getBookById(id)
        return if (resp.isSuccessful) {
            resp.body()
                ?.let { BookMapper.fromDto(it) }
                ?: throw Exception("Boş yanıt")
        } else {
            val err = resp.errorBody()?.string()
            Log.e("BookRepo", "fetchBookById failed: code=${resp.code()}, body=$err")
            throw Exception("Book information could not be loaded: ${resp.code()}")
        }
    }


    suspend fun searchBooks(query: String): List<Book> {
        val resp = api.searchBooks(query)
        return if (resp.isSuccessful) {
            resp.body()
                ?.embedded
                ?.books
                .orEmpty()
                .map { BookMapper.fromDto(it) }
        } else {
            val err = resp.errorBody()?.string()
            Log.e("BookRepo", "searchBooks failed: code=${resp.code()}, body=$err")
            throw Exception("Book search unsuccessful: ${resp.code()}")
        }
    }
    suspend fun getBooksByListId(listId: Long): List<Book> {
        val response = api.getBooksInList(listId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("The listed books could not be retrieved.")
        }
    }
    suspend fun fetchPopularBooks(): List<Book> {
        return apiService.getPopularBooks().map { it.toDomain() }
    }



}
