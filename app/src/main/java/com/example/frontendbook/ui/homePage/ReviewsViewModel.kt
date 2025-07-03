package com.example.frontendbook.ui.homePage

import androidx.lifecycle.*
import com.example.frontendbook.data.model.ReviewDto
import com.example.frontendbook.data.repository.ReviewsRepository
import kotlinx.coroutines.launch

class ReviewsViewModel(
    private val repo: ReviewsRepository
) : ViewModel() {

    private val _reviews = MutableLiveData<List<ReviewDto>>()
    val reviews: LiveData<List<ReviewDto>> = _reviews

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /** Tüm yorumları yükler */
    fun loadAllReviews() {
        viewModelScope.launch {
            try {
                _reviews.value = repo.fetchAllReviews()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Belirli bir kitaba ait yorumları yükler */
    fun loadReviewsForBook(bookId: Long) {
        viewModelScope.launch {
            try {
                _reviews.value = repo.fetchReviewsForBook(bookId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
