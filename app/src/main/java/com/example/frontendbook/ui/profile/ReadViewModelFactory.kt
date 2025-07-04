package com.example.frontendbook.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ReadRepository

class ReadViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReadViewModel::class.java)) {
            val api = RetrofitClient.readApiService(context)
            val repo = ReadRepository(api)
            return ReadViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}