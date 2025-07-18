package com.example.frontendbook.ui.notifications

import androidx.lifecycle.*
import com.example.frontendbook.data.api.dto.NotificationDto
import com.example.frontendbook.data.model.Notification
import com.example.frontendbook.data.model.NotificationType
import com.example.frontendbook.data.repository.NotificationsRepository
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val repo: NotificationsRepository
) : ViewModel() {

    // Arka uçtan gelen ham bildirim listesi (DTO formatında)
    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /** Bildirimleri yükler ve DTO → Notification dönüşümünü yapar */
    fun loadNotifications() {
        viewModelScope.launch {
            try {
                val dtoList = repo.fetchAll()
                val notificationList = dtoList.map { it.toNotification() }
                _notifications.value = notificationList
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Belirli bildirimi okundu olarak işaretler ve listeyi günceller */
    fun markAsRead(notification: Notification) {
        viewModelScope.launch {
            try {
                val ok = repo.markRead(notification.id.toLong())
                if (ok) {
                    _notifications.value = _notifications.value?.map {
                        if (it.id == notification.id) it.copy(read = true) else it
                    }
                } else {
                    _error.value = "İşlem başarısız"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** DTO'dan UI modeli olan Notification nesnesine dönüşüm */
    private fun NotificationDto.toNotification(): Notification {
        return Notification(
            iconResId = 0, // type'a göre NotificationAdapter içinde atanacak
            message = generateMessage(type), // artık content değil
            time = formatTime(createdAt),    // artık timestamp değil
            type = determineNotificationType(type),
            relatedId = targetId,
            id = id,
            read = read
        )
    }

    private fun generateMessage(type: String): String {
        return when (type) {
            "FOLLOW_USER" -> "Seni takip etti"
            "FOLLOW_LIST" -> "Listeni takip etti"
            "LIKE_COMMENT" -> "Yorumunu beğendi"
            else -> "Bildirim"
        }
    }


    /** Mesaja göre NotificationType belirlenir */
    private fun determineNotificationType(message: String): NotificationType {
        return when {
            "takip etti" in message -> NotificationType.FOLLOW
            "yorumunu beğendi" in message -> NotificationType.LIKE_COMMENT
            "listeni beğendi" in message -> NotificationType.FOLLOW_LIST
            else -> NotificationType.FOLLOW
        }
    }

    /** ISO 8601 timestamp'i sadeleştir (örn: 2025-07-04T10:45:00Z → 10:45) */
    private fun formatTime(timestamp: String): String {
        return timestamp.substringAfter("T").substring(0, 5)
    }

    /** Mesajdan ilgili ID'yi çıkarmak için regex veya sabit değer kullanılabilir */
    private fun extractRelatedId(message: String): String {
        // TODO: Gerçek backend formatına göre düzenlenebilir
        return "42"
    }
}