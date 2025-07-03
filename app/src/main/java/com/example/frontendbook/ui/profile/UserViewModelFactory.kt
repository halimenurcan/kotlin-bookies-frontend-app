package com.example.frontendbook.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.UserRepository

class UserViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            // RetrofitClient ve UserRepository örneğini oluştur
            val api        = RetrofitClient.userApiService(context)
            val repository = UserRepository(api)
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
