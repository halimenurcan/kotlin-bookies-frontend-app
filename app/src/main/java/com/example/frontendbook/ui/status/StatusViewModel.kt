package com.example.frontendbook.ui.status

import androidx.lifecycle.*
import com.example.frontendbook.data.model.StatusBook
import com.example.frontendbook.data.repository.StatusRepository
import kotlinx.coroutines.launch

class StatusViewModel(
    private val repo: StatusRepository
) : ViewModel() {

    private val _readBooks = MutableLiveData<List<StatusBook>>()
    val readBooks: LiveData<List<StatusBook>> = _readBooks

    private val _willReadBooks = MutableLiveData<List<StatusBook>>()
    val willReadBooks: LiveData<List<StatusBook>> = _willReadBooks

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadReadBooks(userId: Long) {
        viewModelScope.launch {
            try {
                _readBooks.value = repo.getReadBooks(userId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadWillReadBooks(userId: Long) {
        viewModelScope.launch {
            try {
                _willReadBooks.value = repo.getWillReadBooks(userId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun addReadBook(book: StatusBook) {
        viewModelScope.launch {
            try {
                repo.addReadBook(book)

            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun addWillReadBook(book: StatusBook) {
        viewModelScope.launch {
            try {
                repo.addWillReadBook(book)

            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
