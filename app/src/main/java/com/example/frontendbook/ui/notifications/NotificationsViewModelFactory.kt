package com.example.frontendbook.ui.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.NotificationsRepository
import com.example.frontendbook.data.repository.UserRepository

class NotificationsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            val notifApi = RetrofitClient.notificationApiService(context)
            val userApi = RetrofitClient.userApiService(context)

            val notifRepo = NotificationsRepository(notifApi)
            val userRepo = UserRepository(userApi)

            return NotificationsViewModel(notifRepo, userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
