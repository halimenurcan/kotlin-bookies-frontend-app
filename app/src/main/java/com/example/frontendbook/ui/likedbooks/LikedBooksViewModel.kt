package com.example.frontendbook.ui.likedbooks

import androidx.lifecycle.*
import com.example.frontendbook.data.repository.LikedBooksRepository
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch

class LikedBooksViewModel(
    private val repo: LikedBooksRepository
) : ViewModel() {

    private val _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> = _isLiked

    private val _likedBooks = MutableLiveData<List<Book>>()
    val likedBooks: LiveData<List<Book>> = _likedBooks

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    fun checkLiked(userId: Long, bookId: Long) {
        viewModelScope.launch {
            try {
                _isLiked.value = repo.isBookLiked(userId, bookId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Like check failed"
            }
        }
    }


    fun toggleLike(userId: Long, bookId: Long) {
        viewModelScope.launch {
            try {
                val currently = _isLiked.value ?: false
                val success = if (currently) {
                    repo.unlikeBook(userId, bookId)
                } else {
                    repo.likeBook(userId, bookId)
                }
                if (success) {
                    _isLiked.value = !currently
                } else {
                    _error.value = "Operation failed"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Like operation failed"
            }
        }
    }


    fun loadLikedBooks(userId: Long) {
        viewModelScope.launch {
            try {
                _likedBooks.value = repo.getAllLikedBooks(userId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Likes could not be loaded"
            }
        }
    }
    fun clearError() {
        _error.value = null
    }
}
