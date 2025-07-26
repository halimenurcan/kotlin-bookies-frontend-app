package com.example.frontendbook.data.api.service
import com.example.frontendbook.data.api.dto.EmbeddedBooksResponse
import com.example.frontendbook.data.api.dto.EmbeddedUsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApiService {
    @GET("books/search")
    suspend fun searchBooks(
        @Query("keyword") keyword: String,
        @Query("genres") genres: List<String>? = null,
        @Query("languages") languages: List<String>? = null
    ): Response<EmbeddedBooksResponse>

    @GET("users/search/{keyword}")
    suspend fun searchUsers(@Path("keyword") keyword: String): Response<EmbeddedUsersResponse>
    @GET("books/genres")
    suspend fun getAllGenres(): Response<List<String>>

    @GET("books/languages")
    suspend fun getAllLanguages(): Response<List<String>>
}
