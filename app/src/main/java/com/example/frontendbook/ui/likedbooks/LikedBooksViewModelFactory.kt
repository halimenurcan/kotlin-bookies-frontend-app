package com.example.frontendbook.ui.likedbooks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.LikedBooksRepository

class LikedBooksViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikedBooksViewModel::class.java)) {
            val api        = RetrofitClient.likedBooksApiService(context)
            val repo       = LikedBooksRepository(api)
            return LikedBooksViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
