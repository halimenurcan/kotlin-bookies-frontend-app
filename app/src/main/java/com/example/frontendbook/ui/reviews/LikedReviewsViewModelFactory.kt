package com.example.frontendbook.ui.reviews

import android.content.Context
import androidx.lifecycle.*
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.LikedReviewsRepository

class LikedReviewsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikedReviewsViewModel::class.java)) {
            val api  = RetrofitClient.likedReviewsApiService(context)
            val repo = LikedReviewsRepository(api)
            return LikedReviewsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
