package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.NotificationApiService
import com.example.frontendbook.data.remote.dto.NotificationDto

class NotificationsRepository(
    private val api: NotificationApiService
) {
    suspend fun fetchAll(): List<NotificationDto> {
        val resp = api.getNotifications()
        if (resp.isSuccessful) return resp.body().orEmpty()
        throw Exception("Bildirimler yüklenemedi: ${resp.code()}")
    }

    suspend fun markRead(id: Long): Boolean {
        val resp = api.markAsRead(id)
        return resp.isSuccessful
    }
}
