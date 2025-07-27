package com.example.frontendbook.ui.notifications

import android.util.Log
import androidx.lifecycle.*
import com.example.frontendbook.data.api.dto.NotificationDto
import com.example.frontendbook.data.model.Notification
import com.example.frontendbook.data.model.NotificationType
import com.example.frontendbook.data.repository.NotificationsRepository
import com.example.frontendbook.data.repository.UserRepository
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val repo: NotificationsRepository,
    private val userRepo: UserRepository
) : ViewModel() {


    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadNotifications() {
        viewModelScope.launch {
            try {
                val dtoList = repo.fetchAll()
                val notificationList = dtoList.map { dto ->
                    val senderUsername = userRepo.getUsernameById(dto.senderId)
                    Log.d("USERNAME_DEBUG", "senderId=${dto.senderId} → username=$senderUsername")  // <--- burası
                    dto.toNotification(senderUsername)
                }
                _notifications.value = notificationList
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    fun markAsRead(notification: Notification) {
        viewModelScope.launch {
            try {
                val ok = repo.markRead(notification.id.toLong())
                if (ok) {
                    _notifications.value = _notifications.value?.map {
                        if (it.id == notification.id) it.copy(read = true) else it
                    }
                } else {
                    _error.value = "Operation failed."
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteNotification(notification: Notification) {
        viewModelScope.launch {
            try {
                Log.d("DELETE_NOTIFICATION", "Deleting: id=${notification.id}")
                val response = repo.deleteNotification(notification.id)
                if (response.isSuccessful) {
                    _notifications.value = _notifications.value?.filterNot { it.id == notification.id }
                } else {

                    Log.e("DELETE_NOTIFICATION", "Response code: ${response.code()}")
                }

            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }



    private fun NotificationDto.toNotification(senderUsername: String): Notification {
        return Notification(
            message = generateMessage(type),
            time = formatTime(createdAt),
            type = determineNotificationType(type),
            relatedId = targetId,
            senderUsername = senderUsername,
            id = id,
            read = read
        )
    }


    private fun generateMessage(type: String): String {
        return when (type) {
            "FOLLOW_USER" -> "Followed you"
            "FOLLOW_LIST" -> "Followed your list"
            "LIKE_COMMENT" -> "Liked your review"
            else -> "Notification"
        }
    }



    private fun determineNotificationType(rawType: String): NotificationType {
        return when (rawType.uppercase()) {
            "FOLLOW_USER"  -> NotificationType.FOLLOW
            "LIKE_COMMENT" -> NotificationType.LIKE_COMMENT
            "FOLLOW_LIST"  -> NotificationType.FOLLOW_LIST
            else           -> NotificationType.FOLLOW
        }
    }



    private fun formatTime(timestamp: String): String {
        return timestamp.substringAfter("T").substring(0, 5)
    }


    private fun extractRelatedId(message: String): String {
        // TODO: Gerçek backend formatına göre düzenlenebilir
        return "42"
    }
}