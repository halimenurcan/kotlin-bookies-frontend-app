package com.example.frontendbook.ui.viewmodel

import androidx.lifecycle.*
import com.example.frontendbook.data.repository.BookRepository
import android.util.Log
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {

    private val repository = BookRepository()

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    fun fetchBooks(query: String) {
        viewModelScope.launch {
            try {
                val result = repository.searchBooks(query)

                // Verileri logla
                Log.d("BookViewModel", "Fetched ${result.size} books")
                result.forEach { book ->
                    Log.d("BookViewModel", "Book: ${book.title}, Author: ${book.author}")
                }

                _books.value = result
            } catch (e: Exception) {
                Log.e("BookViewModel", "Fetch failed: ${e.message}")
                _books.value = emptyList()
            }
        }
    }


}
