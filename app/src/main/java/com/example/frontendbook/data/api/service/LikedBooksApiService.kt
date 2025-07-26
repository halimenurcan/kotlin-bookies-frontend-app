package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.data.api.dto.EmbeddedBooksResponse
import com.example.frontendbook.data.model.LikedBookRequest
import com.example.frontendbook.data.model.LikedBookResponse
import com.example.frontendbook.domain.model.Book
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LikedBooksApiService {


    @POST("liked-books")
    suspend fun createLikedBook(
        @Body request: LikedBookRequest
    ): Response<Unit>
    @GET("liked-books/user/{userId}")
    suspend fun getAllLikedBooks(
        @Path("userId") userId: Long
    ): Response<List<BookDto>>

    @GET("liked-books/user/{userId}/book/{bookId}")
    suspend fun getLikedBook(
        @Path("userId") userId: Long,
        @Path("bookId") bookId: Long
    ): Response<LikedBookResponse>


    @DELETE("liked-books/user/{userId}/book/{bookId}")
    suspend fun deleteLikedBook(
        @Path("userId") userId: Long,
        @Path("bookId") bookId: Long
    ): Response<Unit>
}
