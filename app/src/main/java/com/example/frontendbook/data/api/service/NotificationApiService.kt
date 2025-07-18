package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.remote.dto.NotificationResponse
import retrofit2.Response
import retrofit2.http.*

interface NotificationApiService {

    /** Tüm bildirimleri al */
    @GET("notifications")
    suspend fun getNotifications(): Response<NotificationResponse>

    @PATCH("notifications/{id}/read")
    suspend fun markAsRead(
        @Path("id") notificationId: Long
    ): Response<Unit>
}
