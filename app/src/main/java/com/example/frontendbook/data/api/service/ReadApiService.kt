package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.data.api.dto.SimpleReadRequest
import com.example.frontendbook.data.model.ReadEntry
import retrofit2.Response
import retrofit2.http.*

interface ReadApiService {
    @GET("books/{id}")
    suspend fun getBookById(@Path("id") bookId: Long): BookDto
    @GET("books-status/read-list/{userId}")
    suspend fun getReadListByUserId(@Path("userId") userId: Long): List<BookDto>

    @POST("books-status/read-list")
    suspend fun addToReadList(@Body request: SimpleReadRequest): Response<Unit>

    @DELETE("books-status/read-list/user/{userId}/book/{bookId}")
    suspend fun deleteFromReadList(
        @Path("userId") userId: Long,
        @Path("bookId") bookId: Long
    ): Response<Unit>
    @GET("books-status/read/{userId}/book/{bookId}")
    suspend fun isBookRead(@Path("userId") userId: Long, @Path("bookId") bookId: Long): Boolean
    @GET("books-status/read-list/{userId}/book/{bookId}")
    suspend fun isBookReadList(@Path("userId") userId: Long, @Path("bookId") bookId: Long): Boolean


    @GET("books-status/read/{userId}")
    suspend fun getReadByUserId(@Path("userId") userId: Long): List<BookDto>

    @POST("books-status/read")
    suspend fun addToRead(@Body request: SimpleReadRequest): Response<Unit>

    @DELETE("books-status/read/user/{userId}/book/{bookId}")
    suspend fun deleteFromRead(
        @Path("userId") userId: Long,
        @Path("bookId") bookId: Long
    ): Response<Unit>
}
