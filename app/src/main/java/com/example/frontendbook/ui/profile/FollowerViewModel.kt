package com.example.frontendbook.ui.profile

import androidx.lifecycle.*
import com.example.frontendbook.data.api.dto.UserResponse
import com.example.frontendbook.data.repository.FollowersRepository
import kotlinx.coroutines.launch

class FollowerViewModel(
    private val repo: FollowersRepository
) : ViewModel() {

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> = _isFollowing

    // Sadece UserResponse listeleri tut!
    private val _followers = MutableLiveData<List<UserResponse>>()
    val followers: LiveData<List<UserResponse>> = _followers

    private val _following = MutableLiveData<List<UserResponse>>()
    val following: LiveData<List<UserResponse>> = _following

    private val _followersCount = MutableLiveData<Int>()
    val followersCount: LiveData<Int> = _followersCount

    private val _followingCount = MutableLiveData<Int>()
    val followingCount: LiveData<Int> = _followingCount

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
                val ok = if (currently) repo.unfollow(currentUserId, targetUserId)
                else repo.follow(currentUserId, targetUserId)
                if (ok) {
                    loadIsFollowing(targetUserId, currentUserId) // Listeyi güncelle
                } else {
                    _error.value = "İşlem başarısız"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    fun loadFollowers(userId: Long) {
        viewModelScope.launch {
            try {
                // !!! Artık doğrudan UserResponse listesi alıyoruz
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

    fun loadFollowersCount(userId: Long) {
        viewModelScope.launch {
            try {
                _followersCount.value = repo.getFollowerCount(userId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadFollowingCount(userId: Long) {
        viewModelScope.launch {
            try {
                _followingCount.value = repo.getFollowingCount(userId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun loadIsFollowing(targetUserId: Long, currentUserId: Long) {
        viewModelScope.launch {
            try {
                val followersList = repo.getFollowersOfUser(targetUserId)
                // followersList: List<UserResponse>
                _isFollowing.value = followersList.any { it.id == currentUserId }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
