package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.model.AddBookToListRequest
import com.example.frontendbook.data.model.CreateListRequest
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.data.remote.dto.ListWithBooksDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ListsApiService {
    /** Kullanıcının oluşturduğu tüm listeleri getirir */
    @GET("lists/user/{userId}")
    suspend fun getUserLists(
        @Path("userId") userId: Long
    ): Response<List<ListDto>>

    @GET("lists/user/{id}")
    suspend fun getListWithBooksById(
        @Path("id") listId: Long
    ): Response<ListDto>

    @GET("lists/{listId}")
    suspend fun getListWithBooks(@Path("listId") listId: Long): ListDto
    @POST("lists")
    suspend fun createList(
        @Body listRequest: CreateListRequest
    ): Response<Void>

    @POST("books-in-list")
    suspend fun addBookToList(
        @Body request: AddBookToListRequest
    ): Response<Void>

    @DELETE("/api/lists/{id}")
    suspend fun deleteListById(
        @Path("id") listId: Long
    ): Response<Void>

    @GET("/api/lists")
    suspend fun getAllLists(): List<ListDto>

}
