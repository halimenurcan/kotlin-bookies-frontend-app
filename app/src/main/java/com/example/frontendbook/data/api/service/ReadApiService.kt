package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.model.ReadEntry
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReadApiService {

    @GET("readlist/user/{userId}")
    suspend fun getReadListByUserId(@Path("userId") userId: Long): List<ReadEntry>

    @POST("readlist")
    suspend fun addToReadList(@Body readEntry: ReadEntry): ReadEntry

    @DELETE("readlist/{entryId}")
    suspend fun deleteFromReadList(@Path("entryId") entryId: Long)
}