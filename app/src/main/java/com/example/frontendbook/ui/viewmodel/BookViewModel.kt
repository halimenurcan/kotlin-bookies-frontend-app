package com.example.frontendbook.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.frontendbook.data.repository.BookRepository
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BookRepository(application.applicationContext)

    private val _books = MutableLiveData<List<Book>>(emptyList())
    val books: LiveData<List<Book>> = _books
    private val _popularBooks = MutableLiveData<List<Book>>()
    val popularBooks: LiveData<List<Book>> get() = _popularBooks
    /**
     * type:
     *  - "popular", "explore", "fiction" --> tüm kitapları getirir
     *  - aksi halde, searchBooks(query) ile arama yapar
     */
    fun fetchBooks(type: String) {
        viewModelScope.launch {
            try {
                val result: List<Book> = when (type.lowercase()) {
                    "popular", "explore", "fiction" -> {
                        repository.fetchAllBooks()
                    }
                    else -> {
                        // tür değil arama sorgusu
                        repository.searchBooks(type)
                    }
                }

                Log.d("BookViewModel", "Fetched ${result.size} books for type=\"$type\"")
                result.forEach { book ->
                    Log.d("BookViewModel", " • ${book.title} by ${book.author}")
                }

                _books.value = result
            } catch (e: Exception) {
                Log.e("BookViewModel", "Fetch failed for \"$type\": ${e.message}")
                _books.value = emptyList()
            }
        }
    }

    fun fetchBooksInList(listId: Long) {
        viewModelScope.launch {
            try {
                val books = repository.getBooksByListId(listId)
                _books.postValue(books)
            } catch (e: Exception) {
                Log.e("BookViewModel", "Liste kitaplarını getirme hatası", e)
            }
        }
    }
    fun fetchPopularBooks() {
        viewModelScope.launch {
            try {
                val books = repository.fetchPopularBooks()
                _popularBooks.value = books
            } catch (e: Exception) {
                Log.e("BookViewModel", "Popüler kitaplar alınamadı: ${e.message}")
            }
        }
    }

}
