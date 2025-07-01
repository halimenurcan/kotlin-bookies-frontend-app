package com.example.frontendbook.data.api
import com.example.frontendbook.domain.model.googleapi.GoogleBooksResponse

import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiService {
    @GET("/api/books")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int = 0,
        @Query("maxResults") maxResults: Int = 40
    ): GoogleBooksResponse
}
