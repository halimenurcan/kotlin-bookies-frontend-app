package com.example.frontendbook.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.data.remote.BookRepository
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val bookRepository = BookRepository()

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _state = MutableLiveData<SearchUiState>()
    val state: LiveData<SearchUiState> = _state

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun searchBooks(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val results = bookRepository.searchBooks(query)
                _books.value = results
                _errorMessage.value = null
            } catch (e: Exception) {
                _books.value = emptyList()
                _errorMessage.value = "Search failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Opsiyonel alias
    fun fetchBooks(query: String) {
        searchBooks(query)
    }
}
