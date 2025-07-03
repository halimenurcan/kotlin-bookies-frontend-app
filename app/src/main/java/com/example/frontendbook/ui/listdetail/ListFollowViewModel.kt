package com.example.frontendbook.ui.listdetail

import androidx.lifecycle.*
import com.example.frontendbook.data.repository.ListFollowsRepository
import kotlinx.coroutines.launch

class ListFollowViewModel(
    private val repo: ListFollowsRepository
) : ViewModel() {

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> = _isFollowing

    private val _followerCount = MutableLiveData<Int>()
    val followerCount: LiveData<Int> = _followerCount

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadStatus(userId: Long, listId: Long) {
        viewModelScope.launch {
            try {
                _isFollowing.value   = repo.isFollowing(userId, listId)
                _followerCount.value = repo.getFollowerCount(listId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun toggleFollow(userId: Long, listId: Long) {
        viewModelScope.launch {
            try {
                val currently = _isFollowing.value ?: false
                val ok = if (currently) {
                    repo.unfollowList(userId, listId)
                } else {
                    repo.followList(userId, listId)
                }
                if (ok) {
                    // takip durumunu ve sayısını güncelle
                    _isFollowing.value = !currently
                    _followerCount.value =
                        (_followerCount.value ?: 0) + (if (currently) -1 else +1)
                } else {
                    _error.value = "İşlem başarısız"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
