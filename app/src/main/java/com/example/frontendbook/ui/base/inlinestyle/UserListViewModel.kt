package com.example.frontendbook.ui.base.inlinestyle

import androidx.lifecycle.*
import com.example.frontendbook.data.remote.dto.UserDto
import com.example.frontendbook.data.repository.UserFollowRepository
import com.example.frontendbook.domain.model.UserListType
import com.example.frontendbook.data.model.UserSimple
import kotlinx.coroutines.launch

class UserListViewModel(
    private val repo: UserFollowRepository
) : ViewModel() {

    private val _users = MutableLiveData<List<UserSimple>>()
    val users: LiveData<List<UserSimple>> = _users

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadList(profileUserId: Long, type: UserListType) {
        viewModelScope.launch {
            try {
                val dtos: List<UserDto> = when (type) {
                    UserListType.FOLLOWERS -> repo.getFollowers(profileUserId)
                    UserListType.FOLLOWING -> repo.getFollowing(profileUserId)
                }
                _users.value = dtos.map {
                    UserSimple(
                        userId    = it.id.toString(),
                        username  = it.username ?: "",
                        avatarUrl = it.profileImageUrl
                    )
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
            }
        }
}