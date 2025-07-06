package com.example.frontendbook.ui.homePage

import androidx.lifecycle.*
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.model.ReviewDto
import com.example.frontendbook.data.repository.ReviewsRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for handling review-related UI state and operations.
 */
class ReviewsViewModel(
    private val repo: ReviewsRepository
) : ViewModel() {

    private val _comments = MutableLiveData<List<ReviewDto>>(emptyList())
    val comments: LiveData<List<ReviewDto>> = _comments

    private val _single = MutableLiveData<ReviewDto>()
    val single: LiveData<ReviewDto> = _single

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    /** Belirli bir kitaba ait yorumları yükler */
    fun loadCommentsForBook(bookId: Long) {
        viewModelScope.launch {
            try {
                // Loglama repository içinde yapılıyor
                val list = repo.fetchReviewsForBook(bookId)
                _comments.value = list
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Tek bir yorumu yükler */
    fun loadComment(id: Long) {
        viewModelScope.launch {
            try {
                val comment = repo.getCommentById(id)
                _single.value = comment
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Yeni yorum oluşturur */
    fun createComment(request: ReviewCreateRequest) {
        viewModelScope.launch {
            try {
                val created = repo.createComment(request)
                // Oluşturulan yorumu liste başına ekle
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
    fun loadAllReviews() {
        viewModelScope.launch {
            try {
                _comments.value = repo.fetchAllReviews()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

