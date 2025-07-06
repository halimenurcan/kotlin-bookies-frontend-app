package com.example.frontendbook.data.repository

import android.content.Context
import android.util.Log
import com.example.frontendbook.data.api.dto.EmbeddedBooksResponse
import com.example.frontendbook.data.api.mapper.BookMapper
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.domain.model.Book

class BookRepository(context: Context) {
    private val api = RetrofitClient.booksApiService(context)

    /** Tüm kitapları getir ve domain model’e map et */
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
            throw Exception("Kitaplar yüklenemedi: ${resp.code()}")
        }
    }

    /** Tek bir kitabı getir ve domain model’e map et */
    suspend fun fetchBookById(id: Long): Book {
        val resp = api.getBookById(id)
        return if (resp.isSuccessful) {
            resp.body()
                ?.let { BookMapper.fromDto(it) }
                ?: throw Exception("Boş yanıt")
        } else {
            val err = resp.errorBody()?.string()
            Log.e("BookRepo", "fetchBookById failed: code=${resp.code()}, body=$err")
            throw Exception("Kitap bilgisi yüklenemedi: ${resp.code()}")
        }
    }

    /** Arama yapmak için HAL wrapper’da gömülü kitap listesini alıp map et */
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
            throw Exception("Kitap arama başarısız: ${resp.code()}")
        }
    }
}
