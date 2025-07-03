// app/src/main/java/com/example/frontendbook/ui/reviews/LikedReviewsViewModel.kt
package com.example.frontendbook.ui.reviews

import androidx.lifecycle.*
import com.example.frontendbook.data.repository.LikedReviewsRepository
import kotlinx.coroutines.launch

class LikedReviewsViewModel(
    private val repo: LikedReviewsRepository
) : ViewModel() {

    private val _isLiked    = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean> = _isLiked

    private val _likeCount  = MutableLiveData<Int>()
    val likeCount: LiveData<Int> = _likeCount

    private val _error      = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /** İnceleme ID’si için beğeni durumunu ve sayısını yükle */
    fun loadStatus(userId: Long, reviewId: Long) {
        viewModelScope.launch {
            try {
                _isLiked.value   = repo.isLiked(userId, reviewId)
                _likeCount.value = repo.getLikeCount(reviewId)
                _error.value     = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Toggle like/unlike, sonra sayıyı güncelle */
    fun toggleLike(userId: Long, reviewId: Long) {
        viewModelScope.launch {
            try {
                val currently = _isLiked.value ?: false
                val ok = if (currently) {
                    repo.unlike(userId, reviewId)
                } else {
                    repo.like(userId, reviewId)
                }
                if (ok) {
                    _isLiked.value = !currently
                    _likeCount.value = (_likeCount.value ?: 0) + if (currently) -1 else +1
                } else {
                    _error.value = "İşlem başarısız"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
