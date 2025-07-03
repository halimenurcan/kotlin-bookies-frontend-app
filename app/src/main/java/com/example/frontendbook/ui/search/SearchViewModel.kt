package com.example.frontendbook.ui.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.data.repository.BookRepository
import com.example.frontendbook.domain.model.CombinedSearchResult
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val bookRepository = BookRepository(application.applicationContext)

    private val _combinedResults = MutableLiveData<List<CombinedSearchResult>>()
    val combinedResults: LiveData<List<CombinedSearchResult>> = _combinedResults

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    fun searchBooks(query: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val bookResults = bookRepository.searchBooks(query)
                val combined = bookResults.map { CombinedSearchResult.BookResult(it) }
                _combinedResults.value = combined
                _errorMessage.value = null
            } catch (e: Exception) {
                _combinedResults.value = emptyList()
                _errorMessage.value = "Kitap arama hatası: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchBooksAndUsers(query: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                Log.d("SearchDebug", "🔍 Arama başlatıldı: $query")

                val bookResults = bookRepository.searchBooks(query)
                val userResults = bookRepository.searchUsers(query)

                Log.d("SearchDebug", "📚 Kitap sayısı: ${bookResults.size}")
                Log.d("SearchDebug", "👤 Kullanıcı sayısı: ${userResults.size}")

                val combined = mutableListOf<CombinedSearchResult>()
                combined.addAll(userResults.map { CombinedSearchResult.UserResult(it) })
                combined.addAll(bookResults.map { CombinedSearchResult.BookResult(it) })

                _combinedResults.value = combined
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e("SearchDebug", " Arama hatası: ${e.message}", e)
                _combinedResults.value = emptyList()
                _errorMessage.value = "Arama başarısız: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
