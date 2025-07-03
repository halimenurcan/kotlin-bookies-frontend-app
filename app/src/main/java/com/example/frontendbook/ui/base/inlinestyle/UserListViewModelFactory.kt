package com.example.frontendbook.ui.base.inlinestyle

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.UserFollowRepository

class UserListViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
            val api  = RetrofitClient.userApiService(context)
            val repo = UserFollowRepository(api)
            return UserListViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
