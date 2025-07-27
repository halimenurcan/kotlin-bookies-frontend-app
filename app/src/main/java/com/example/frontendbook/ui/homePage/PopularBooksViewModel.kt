package com.example.frontendbook.ui.homePage

import androidx.lifecycle.*
import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.data.repository.BookRepository
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch

class PopularBooksViewModel(private val repository: BookRepository) : ViewModel() {

    private val _popularBooks = MutableLiveData<List<Book>>()
    val popularBooks: LiveData<List<Book>> get() = _popularBooks

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadPopularBooks() {
        viewModelScope.launch {
            try {
                val books = repository.fetchPopularBooks()
                _popularBooks.value = books
            } catch (e: Exception) {
                _error.value = "Popular books could not be loaded: ${e.message}"
            }
        }
    }
}
