package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.model.ListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ListsApiService {
    /** Kullanıcının oluşturduğu tüm listeleri getirir */
    @GET("api/lists/user/{userId}")
    suspend fun getUserLists(
        @Path("userId") userId: Long
    ): Response<List<ListDto>>
}
