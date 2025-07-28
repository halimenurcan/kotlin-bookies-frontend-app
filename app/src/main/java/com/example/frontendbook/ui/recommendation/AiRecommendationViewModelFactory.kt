package com.example.frontendbook.ui.recommendation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.AiRepository
import com.example.frontendbook.ui.base.threecolumn.AiRecommendationViewModel

class AiRecommendationViewModelFactory(
    private val context: Context,
    private val aiRepo: AiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AiRecommendationViewModel(context, aiRepo) as T
    }
}