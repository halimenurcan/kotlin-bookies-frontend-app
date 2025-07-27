package com.example.frontendbook.ui.listdetail

import android.util.Log
import androidx.lifecycle.*
import com.example.frontendbook.data.repository.ListFollowsRepository
import kotlinx.coroutines.launch

class ListFollowViewModel(
    private val repo: ListFollowsRepository,
    val followedListIds: MutableLiveData<List<Long>> = MutableLiveData()
) : ViewModel() {

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun toggleFollow(currentUserId: Long, listId: Long) {
        viewModelScope.launch {
            try {
                val currentlyFollowed = followedListIds.value?.contains(listId) ?: false
                Log.d("ListFollowVM", "toggleFollow: listId=$listId, currently=$currentlyFollowed")

                val result = if (currentlyFollowed) {
                    repo.unfollowList(currentUserId, listId).also {
                        Log.d("ListFollowVM", "Unfollow result: $it")
                    }
                } else {
                    repo.followList(currentUserId, listId).also {
                        Log.d("ListFollowVM", "Follow result: $it")
                    }
                }

                if (result) {
                    Log.d("ListFollowVM", "Toggle succeeded, reloading followed IDs...")
                    loadFollowedListIds(currentUserId)
                } else {
                    _error.value = "Operation failed"
                    Log.w("ListFollowVM", "Toggle failed")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ListFollowVM", "toggleFollow error: ${e.message}", e)
            }
        }
    }

    fun loadFollowedListIds(userId: Long) {
        viewModelScope.launch {
            try {
                val ids = repo.getFollowedListIdsByUser(userId)
                Log.d("ListFollowVM", "Incoming tracked IDs: $ids")
                followedListIds.postValue(ids.distinct())
            } catch (e: Exception) {
                Log.e("ListFollowVM", "loadFollowedListIds error: ${e.message}", e)
            }
        }
    }
}
