package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.NotificationDto
import com.example.frontendbook.data.remote.dto.NotificationResponse
import retrofit2.Response
import retrofit2.http.*

import retrofit2.http.*

interface NotificationApiService {

    // NotificationApiService.kt
    @GET("notifications")
    suspend fun getAllNotifications(): NotificationResponse


    @PATCH("notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") id: Long)

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Long): Response<Unit>


    @GET("notifications/count/unread")
    suspend fun getUnreadCount(): Int
}

