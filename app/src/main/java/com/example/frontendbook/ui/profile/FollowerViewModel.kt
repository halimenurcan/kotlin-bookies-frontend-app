package com.example.frontendbook.ui.profile

import androidx.lifecycle.*
import com.example.frontendbook.data.repository.FollowersRepository
import kotlinx.coroutines.launch

class FollowerViewModel(
    private val repo: FollowersRepository
) : ViewModel() {

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> = _isFollowing

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /** İlk açılışta takip durumunu çek */
    fun loadFollowStatus(userId: Long, followerId: Long) {
        viewModelScope.launch {
            try {
                _isFollowing.value = repo.isFollowing(userId, followerId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    /** Butona tıklayınca toggle et */
    fun toggleFollow(userId: Long, followerId: Long) {
        viewModelScope.launch {
            try {
                val currently = _isFollowing.value ?: false
                val ok = if (currently) {
                    repo.unfollow(userId, followerId)
                } else {
                    repo.follow(userId, followerId)
                }
                if (ok) {
                    _isFollowing.value = !currently
                } else {
                    _error.value = "İşlem başarısız"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
