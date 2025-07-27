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

class ReviewsViewModel(private val repo: ReviewsRepository, private val context: Context) : ViewModel() {

    private val _comments = MutableLiveData<List<ReviewDto>>(emptyList())
    val comments: LiveData<List<ReviewDto>> = _comments

    private val _selectedReview = MutableLiveData<ReviewDto?>()
    val selectedReview: LiveData<ReviewDto?> = _selectedReview

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadAllComments() {
        viewModelScope.launch {
            try {
                Log.d("REVIEW_VM", "Loading all comments")
                _comments.value = repo.fetchAllComments()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("REVIEW_VM", "Error loading all comments: ${e.message}")
            }
        }
    }

    fun loadCommentsForBook(bookId: Long) {
        viewModelScope.launch {
            try {
                Log.d("REVIEW_VM", "Loading comments for bookId=$bookId")
                _comments.value = repo.fetchReviewsForBook(bookId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("REVIEW_VM", "Error loading comments for book: ${e.message}")
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
    fun loadCommentById(id: Long) {
        viewModelScope.launch {
            try {
                Log.d("REVIEW_VM", "Loading comment by id=$id")
                _selectedReview.value = repo.getCommentById(id)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("REVIEW_VM", "Error loading comment by id: ${e.message}")
            }
        }
    }
}
