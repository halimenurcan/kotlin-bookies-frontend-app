package com.example.frontendbook.ui.base.threecolumn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.data.repository.ListsRepository

class ThreeColumnViewModelFactory(
    private val repository: ListsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThreeColumnViewModel::class.java)) {
            return ThreeColumnViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}