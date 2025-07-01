package com.example.frontendbook.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.data.repository.BookRepository
import com.example.frontendbook.domain.model.CombinedSearchResult
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val bookRepository = BookRepository()

    // Arama sonuçlarını kitap + kullanıcı olarak birleştir
    private val _combinedResults = MutableLiveData<List<CombinedSearchResult>>()
    val combinedResults: LiveData<List<CombinedSearchResult>> = _combinedResults

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun searchBooksAndUsers(query: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                Log.d("SearchDebug", "Started search with query: $query")

                val bookResults = bookRepository.searchBooks(query)
                Log.d("SearchDebug", "Book results: ${bookResults.size}")

                val userResults = bookRepository.searchUsers(query)
                Log.d("SearchDebug", "User results: ${userResults.size}")

                val combined = mutableListOf<CombinedSearchResult>()
                combined.addAll(userResults.map { CombinedSearchResult.UserResult(it) })
                combined.addAll(bookResults.map { CombinedSearchResult.BookResult(it) })

                _combinedResults.value = combined
                _errorMessage.value = null
            } catch (e: Exception) {
                _combinedResults.value = emptyList()
                _errorMessage.value = "Arama başarısız: ${e.message}"
                Log.e("SearchDebug", "Search error: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }

    }
}
