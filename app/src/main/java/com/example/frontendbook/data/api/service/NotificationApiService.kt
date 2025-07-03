package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.remote.dto.NotificationDto
import retrofit2.Response
import retrofit2.http.*

interface NotificationApiService {

    /** Tüm bildirimleri al */
    @GET("api/notifications")
    suspend fun getNotifications(): Response<List<NotificationDto>>

    /** Tek bir bildirimi “okundu” olarak işaretle */
    @PATCH("api/notifications/{id}/read")
    suspend fun markAsRead(
        @Path("id") notificationId: Long
    ): Response<Unit>
}
