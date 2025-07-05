package com.example.frontendbook.data.remote

import com.example.frontendbook.data.model.register.RegisterRequest
import com.example.frontendbook.data.model.register.RegisterResponse
import com.example.frontendbook.data.model.signIn.SignInRequest
import com.example.frontendbook.data.model.signIn.SignInResponse
import com.example.frontendbook.domain.model.Book
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/login")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>


    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    // 📚 BOOKS - Sıralama
    @GET("/books/sorted")
    suspend fun getSortedBooks(
        @Query("sort") sort: String // "most_popular", "highly_rated"
    ): Response<List<Book>>

    // 📚 BOOKS - Arama
    @GET("/books/search")
    suspend fun searchBooks(
        @Query("query") query: String
    ): Response<List<Book>>

    // 📚 BOOKS - Filtreleme
    @GET("/books/filter")
    suspend fun filterBooks(
        @Query("genre") genre: String? = null,
        @Query("country") country: String? = null,
        @Query("author") author: String? = null,
        @Query("language") language: String? = null
    ): Response<List<Book>>
}
