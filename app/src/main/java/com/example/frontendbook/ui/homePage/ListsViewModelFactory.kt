package com.example.frontendbook.ui.homePage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ListsRepository

class ListsViewModelFactory(
private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListsViewModel::class.java)) {
            val api = RetrofitClient.listsApiService(context)
            val repo = ListsRepository(api)
            return ListsViewModel(context, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

