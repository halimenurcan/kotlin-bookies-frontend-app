package com.example.frontendbook.ui.notifications

import androidx.lifecycle.*
import com.example.frontendbook.data.remote.dto.NotificationDto
import com.example.frontendbook.data.repository.NotificationsRepository
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val repo: NotificationsRepository
) : ViewModel() {

    private val _notifications = MutableLiveData<List<NotificationDto>>()
    val notifications: LiveData<List<NotificationDto>> = _notifications

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /** Bildirimleri yükle */
    fun loadNotifications() {
        viewModelScope.launch {
            try {
                _notifications.value = repo.fetchAll()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Belirli bildirimi okundu olarak işaretle (ve listede güncelle) */
    fun markAsRead(notification: NotificationDto) {
        viewModelScope.launch {
            try {
                val ok = repo.markRead(notification.id)
                if (ok) {
                    _notifications.value = _notifications.value
                        ?.map { if (it.id == notification.id) it.copy(read = true) else it }
                } else {
                    _error.value = "İşlem başarısız"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
