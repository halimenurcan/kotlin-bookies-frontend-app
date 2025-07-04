package com.example.frontendbook.ui.listdetail

import androidx.lifecycle.*
import com.example.frontendbook.data.repository.ListFollowsRepository
import kotlinx.coroutines.launch

class ListFollowViewModel(
    private val repo: ListFollowsRepository
) : ViewModel() {

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun toggleFollow(userId: Long, listId: Long) {
        viewModelScope.launch {
            try {
                val currentlyFollowing = repo.isFollowing(userId, listId)
                val success = if (currentlyFollowing) {
                    repo.unfollowList(userId, listId)
                } else {
                    repo.followList(userId, listId)
                }
                if (!success) {
                    _error.value = "Takip işlemi başarısız"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
