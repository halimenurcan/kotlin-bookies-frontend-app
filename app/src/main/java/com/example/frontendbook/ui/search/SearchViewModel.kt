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

    private val _genres = MutableLiveData<List<String>>()
    val genres: LiveData<List<String>> = _genres

    private val _languages = MutableLiveData<List<String>>()
    val languages: LiveData<List<String>> = _languages

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
        _searchStarted.postValue(true)
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val results = repo.searchAll(query, genres, languages)
                _combinedResults.postValue(results)
                _error.postValue(null)
            } catch (e: Exception) {
                _combinedResults.postValue(emptyList())
                _error.postValue(e.message ?: "An error occurred")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun clearResults() {
        _combinedResults.postValue(emptyList())
        _searchStarted.postValue(false)
    }

    fun loadFilterOptions() {
        viewModelScope.launch {
            try {
                val genreList = repo.getGenres()
                val languageList = repo.getLanguages()
                _genres.postValue(genreList)
                _languages.postValue(languageList)
            } catch (e: Exception) {
                _error.postValue("Filters could not load: ${e.message}")
            }
        }
    }
}