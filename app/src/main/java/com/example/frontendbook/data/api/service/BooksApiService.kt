// app/src/main/java/com/example/frontendbook/data/api/service/BooksApiService.kt
package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.EmbeddedBooksResponse
import com.example.frontendbook.data.remote.dto.BookDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApiService {
    @GET("books")
    suspend fun getAllBooks(): Response<EmbeddedBooksResponse>

    @GET("books/{id}")
    suspend fun getBookById(@Path("id") id: Long): Response<BookDto>

    @GET("books/search")
    suspend fun searchBooks(@Query("q") query: String): Response<EmbeddedBooksResponse>
}
