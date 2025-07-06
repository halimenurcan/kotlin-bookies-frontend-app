// app/src/main/java/com/example/frontendbook/data/api/service/BooksApiService.kt
package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.EmbeddedBooksResponse
import com.example.frontendbook.data.api.dto.BookDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BooksApiService {
    @GET("books")
    suspend fun getAllBooks(): Response<EmbeddedBooksResponse>

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: Long): Response<BookDto>

    @GET("books/search/{query}")
    suspend fun searchBooks(@Path("query") query: String): Response<EmbeddedBooksResponse>}

