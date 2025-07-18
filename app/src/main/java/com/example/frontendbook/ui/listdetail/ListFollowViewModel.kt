package com.example.frontendbook.ui.listdetail

import android.util.Log
import androidx.lifecycle.*
import com.example.frontendbook.data.repository.ListFollowsRepository
import kotlinx.coroutines.launch

class ListFollowViewModel(
    private val repo: ListFollowsRepository,
    val followedListIds: MutableLiveData<List<Long>> = MutableLiveData<List<Long>>()


) : ViewModel() {

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun toggleFollow(currentUserId: Long, listId: Long) {
        viewModelScope.launch {
            try {
                val currently = followedListIds.value?.contains(listId) ?: false
                Log.d("ListFollowVM", "toggleFollow: currently=$currently for listId=$listId")
                val ok = if (currently) repo.unfollowList(currentUserId, listId)
                else repo.followList(currentUserId, listId)
                if (ok) {
                    // Güncel listeyi tekrar çekiyoruz!
                    loadFollowedListIds(currentUserId)
                    Log.d("ListFollowVM", "Toggle success, list updated")
                } else {
                    _error.value = "İşlem başarısız"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun loadFollowedListIds(userId: Long) {
        viewModelScope.launch {
            try {
                val ids = repo.getFollowedListIdsByUser(userId)
                followedListIds.value = ids
            } catch (e: Exception) {
            }
        }}
}
