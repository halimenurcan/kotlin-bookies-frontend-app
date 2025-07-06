package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.data.api.dto.EmbeddedBooksResponse
import com.example.frontendbook.data.api.dto.EmbeddedUsersResponse
import com.example.frontendbook.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchApiService {
    @GET("books/search/{keyword}")
    suspend fun searchBooks(@Path("keyword") keyword: String): Response<EmbeddedBooksResponse>

    @GET("users/search/{keyword}")
    suspend fun searchUsers(@Path("keyword") keyword: String): Response<EmbeddedUsersResponse>
}
