package com.example.frontendbook.ui.homePage

import androidx.lifecycle.*
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.repository.ReviewsRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for handling review-related UI state and operations.
 */
class ReviewsViewModel(
    private val repo: ReviewsRepository
) : ViewModel() {

    // — LIST of comments/reviews —
    private val _comments = MutableLiveData<List<ReviewDto>>(emptyList())
    val comments: LiveData<List<ReviewDto>> = _comments

    // — SINGLE comment/review —
    private val _selectedReview = MutableLiveData<ReviewDto?>()
    val selectedReview: LiveData<ReviewDto?> = _selectedReview

    // — ERROR messages —
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    /** Yorum listesini (tüm yorumlar) yükler */
    fun loadAllComments() {
        viewModelScope.launch {
            try {
                _comments.value = repo.fetchAllComments()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Bir kitaba ait yorumları yükler */
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

    /** Tek bir yorumu yükler ve [selectedReview]’e yayınlar */
    fun loadCommentById(id: Long) {
        viewModelScope.launch {
            try {
                _selectedReview.value = repo.getCommentById(id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Yeni yorum oluşturur ve liste başına ekler */
    fun createComment(request: ReviewCreateRequest) {
        viewModelScope.launch {
            try {
                val created = repo.createComment(request)
                _comments.value = listOf(created) + (_comments.value ?: emptyList())
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Yorum silme işlemi */
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
