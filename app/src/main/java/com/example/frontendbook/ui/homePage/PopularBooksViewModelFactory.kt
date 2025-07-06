package com.example.frontendbook.ui.homePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.repository.BookRepository

class PopularBooksViewModelFactory(
    private val repository: BookRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopularBooksViewModel::class.java)) {
            return PopularBooksViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
