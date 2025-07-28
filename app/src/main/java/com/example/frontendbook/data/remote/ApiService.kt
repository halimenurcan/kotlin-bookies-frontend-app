package com.example.frontendbook.data.remote

import com.example.frontendbook.data.api.dto.OpenAiDto.RunStatusResponse
import com.example.frontendbook.data.model.register.RegisterRequest
import com.example.frontendbook.data.model.register.RegisterResponse
import com.example.frontendbook.data.model.signIn.SignInRequest
import com.example.frontendbook.data.model.signIn.SignInResponse
import com.example.frontendbook.domain.model.Book
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/api/auth/login")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>


    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>


    @GET("/books/sorted")
    suspend fun getSortedBooks(
        @Query("sort") sort: String
    ): Response<List<Book>>


    @GET("/books/search")
    suspend fun searchBooks(
        @Query("query") query: String
    ): Response<List<Book>>



}
