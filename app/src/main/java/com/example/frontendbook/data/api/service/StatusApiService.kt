package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.model.StatusBook
import retrofit2.Response
import retrofit2.http.*

interface StatusApiService {

    @POST("statueses/read")
    suspend fun addReadBook(@Body statusBook: StatusBook): Response<StatusBook>

    @POST("statueses/willread")
    suspend fun addWillReadBook(@Body statusBook: StatusBook): Response<StatusBook>

    @GET("statueses/read/{userId}")
    suspend fun getReadBooks(@Path("userId") userId: Long): Response<List<StatusBook>>

    @GET("statueses/willread/{userId}")
    suspend fun getWillReadBooks(@Path("userId") userId: Long): Response<List<StatusBook>>}
