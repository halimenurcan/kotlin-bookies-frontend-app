package com.example.frontendbook.ui.search
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.frontendbook.data.repository.SearchRepository
import com.example.frontendbook.domain.model.CombinedSearchResult
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: SearchRepository
) : ViewModel() {

    private val _combinedResults = MutableLiveData<List<CombinedSearchResult>>()
    val combinedResults: LiveData<List<CombinedSearchResult>> = _combinedResults

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _error
    private val _searchStarted = MutableLiveData(false)


    val searchStarted: LiveData<Boolean> = _searchStarted
    fun searchBooksAndUsers(
        query: String,
        genres: List<String>? = null,
        languages: List<String>? = null
    ) {
        _searchStarted.value = true
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _combinedResults.value = repo.searchAll(query, genres, languages)
                _error.value = null
            } catch (e: Exception) {
                _combinedResults.value = emptyList()
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearResults() {
        _combinedResults.value = emptyList()
        _searchStarted.value = false
    }


}
