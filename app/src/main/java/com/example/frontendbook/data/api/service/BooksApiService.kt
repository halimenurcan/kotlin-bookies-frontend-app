package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.EmbeddedBooksResponse
import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.domain.model.Book
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BooksApiService {
    @GET("books")
    suspend fun getAllBooks(): Response<EmbeddedBooksResponse>

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: Long): Response<BookDto>

    @GET("books/search/{query}")
    suspend fun searchBooks(@Path("query") query: String): Response<EmbeddedBooksResponse>
    @GET("lists/{listId}/books")
    suspend fun getBooksInList(@Path("listId") listId: Long): Response<List<Book>>

    @GET("/api/books/popular")
    suspend fun getPopularBooks(): List<BookDto>


}


