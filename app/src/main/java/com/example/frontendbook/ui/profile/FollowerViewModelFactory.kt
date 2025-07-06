package com.example.frontendbook.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.FollowersRepository

class FollowerViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowerViewModel::class.java)) {
            val api  = RetrofitClient.followersApiService(context)
            val repo = FollowersRepository(api)
            return FollowerViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
        }
}