package com.example.frontendbook.ui.base.threecolumn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.data.repository.ListsRepository
import kotlinx.coroutines.launch

class ThreeColumnViewModel(private val repository: ListsRepository) : ViewModel() {

    private val _listWithBooks = MutableLiveData<ListDto>()
    val listWithBooks: LiveData<ListDto> = _listWithBooks

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    fun fetchListWithBooks(listId: Long) {
        viewModelScope.launch {
            try {
                val result = repository.getListWithBooks(listId)
                _listWithBooks.postValue(result)
            } catch (e: Exception) {
                _error.postValue(" Error: ${e.message}")
            }
        }
    }
}


