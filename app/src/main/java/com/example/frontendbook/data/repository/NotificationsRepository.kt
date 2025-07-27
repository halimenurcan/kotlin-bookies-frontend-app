package com.example.frontendbook.data.repository


import com.example.frontendbook.data.api.service.NotificationApiService
import com.example.frontendbook.data.api.dto.NotificationDto
import retrofit2.Response

class NotificationsRepository(private val api: NotificationApiService) {

    suspend fun fetchAll(): List<NotificationDto> {
        return try {
            val response = api.getAllNotifications()
            response.embedded?.notifications ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun markRead(notificationId: Long): Boolean {
        return try {
            api.markNotificationAsRead(notificationId)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteNotification(notificationId: Long): Response<Unit> {
        return api.deleteNotification(notificationId)
    }


    suspend fun getUnreadCount(): Int {
        return api.getUnreadCount()
    }
}

