package com.example.frontendbook.ui.listdetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ListFollowsRepository

class ListFollowViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(cls: Class<T>): T {
        if (cls.isAssignableFrom(ListFollowViewModel::class.java)) {
            val api  = RetrofitClient.listFollowsApiService(context)
            val repo = ListFollowsRepository(api)
            return ListFollowViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}
