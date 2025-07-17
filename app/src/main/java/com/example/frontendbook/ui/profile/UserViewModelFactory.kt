package com.example.frontendbook.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.UserRepository
import com.example.frontendbook.data.repository.FollowersRepository

class UserViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            val userApi = RetrofitClient.userApiService(context)
            val followersApi = RetrofitClient.followersApiService(context)
            val userRepository = UserRepository(userApi)
            val followersRepository = FollowersRepository(followersApi)
            return UserViewModel(
                userRepository,
                followersRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
