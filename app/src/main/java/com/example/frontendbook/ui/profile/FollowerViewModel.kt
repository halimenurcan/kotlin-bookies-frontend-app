package com.example.frontendbook.ui.profile

import androidx.lifecycle.*
import com.example.frontendbook.data.model.FollowerResponse
import com.example.frontendbook.data.repository.FollowersRepository
import kotlinx.coroutines.launch

class FollowerViewModel(
    private val repo: FollowersRepository
) : ViewModel() {

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> = _isFollowing

    private val _followers = MutableLiveData<List<FollowerResponse>>()
    val followers: LiveData<List<FollowerResponse>> = _followers

    private val _following = MutableLiveData<List<FollowerResponse>>()
    val following: LiveData<List<FollowerResponse>> = _following

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadFollowStatus(userId: Long, followerId: Long) {
        viewModelScope.launch {
            try {
                _isFollowing.value = repo.isFollowing(userId, followerId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun toggleFollow(currentUserId: Long, targetUserId: Long) {
        viewModelScope.launch {
            try {
                val currently = _isFollowing.value ?: false
                val ok = if (currently) repo.unfollow(currentUserId, targetUserId) else repo.follow(currentUserId, targetUserId)
                if (ok) _isFollowing.value = !currently
                else _error.value = "İşlem başarısız"
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadFollowers(userId: Long) {
        viewModelScope.launch {
            try {
                _followers.value = repo.getFollowersOfUser(userId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadFollowing(userId: Long) {
        viewModelScope.launch {
            try {
                _following.value = repo.getFollowingOfUser(userId)
            } catch (e: Exception) {
                _error.value = e.message
            }
            }
        }
}