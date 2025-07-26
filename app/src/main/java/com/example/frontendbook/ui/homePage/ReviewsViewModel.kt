package com.example.frontendbook.ui.homePage

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.LikedReviewsRepository
import com.example.frontendbook.data.repository.ReviewsRepository
import kotlinx.coroutines.launch

class ReviewsViewModel(
    private val repo: ReviewsRepository,
    private val context: Context // → context eklendi!
) : ViewModel() {

    private val _comments = MutableLiveData<List<ReviewDto>>(emptyList())
    val comments: LiveData<List<ReviewDto>> = _comments

    private val _selectedReview = MutableLiveData<ReviewDto?>()
    val selectedReview: LiveData<ReviewDto?> = _selectedReview

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

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
    fun loadAllCommentsWithLikes(userId: Long) {
        viewModelScope.launch {
            try {
                val rawList = repo.fetchAllComments()

                val likedRepo = LikedReviewsRepository(RetrofitClient.likedReviewsApiService(context))
                val likedIds = likedRepo.getLikedReviewIds(userId)

                val enriched = rawList.map { review ->
                    review.copy(isLiked = likedIds.contains(review.id))
                }

                Log.d("VIEWMODEL", "Liked Ids: $likedIds")
                Log.d("VIEWMODEL", "Enriched: ${enriched.map { it.id to it.isLiked }}")

                _comments.value = enriched
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("VIEWMODEL", "Error loading comments with likes: ${e.message}")
            }
        }
    }


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
