package com.example.frontendbook.ui.homePage

import androidx.lifecycle.*
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.model.ReviewDto
import com.example.frontendbook.data.repository.ReviewsRepository
import kotlinx.coroutines.launch


class ReviewsViewModel(
    private val repo: ReviewsRepository
) : ViewModel() {

    private val _comments = MutableLiveData<List<ReviewDto>>()
    val comments: LiveData<List<ReviewDto>> = _comments

    private val _single   = MutableLiveData<ReviewDto>()
    val single: LiveData<ReviewDto> = _single

    private val _error    = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadCommentsForBook(bookId: Long) {
        viewModelScope.launch {
            try {
                _comments.value = repo.fetchReviewsForBook(bookId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadComment(id: Long) {
        viewModelScope.launch {
            try {
                _single.value = repo.getCommentById(id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun createComment(request: ReviewCreateRequest) {
        viewModelScope.launch {
            try {
                val created = repo.createComment(request)
                // Oluşturulanı listemize ekleyelim
                _comments.value = listOf(created) + (_comments.value ?: emptyList())
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteComment(id: Long) {
        viewModelScope.launch {
            try {
                repo.deleteReview(id)
                _comments.value = _comments.value?.filter { it.id != id }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

