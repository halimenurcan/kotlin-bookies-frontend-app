package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.model.AddBookRequest
import com.example.frontendbook.data.model.CreateListRequest
import com.example.frontendbook.data.model.ListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ListsApiService {
    /** Kullanıcının oluşturduğu tüm listeleri getirir */
    @GET("api/lists/user/{userId}")
    suspend fun getUserLists(
        @Path("userId") userId: Long
    ): Response<List<ListDto>>

    @POST("api/lists")
    suspend fun createList(
        @Body listRequest: CreateListRequest
    ): Response<Void>

    @POST("api/lists/{listId}/books")
    suspend fun addBookToList(
        @Path("listId") listId: Long,
        @Body bookRequest: AddBookRequest
    ): Response<Void>

}
