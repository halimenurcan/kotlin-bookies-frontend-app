package com.example.frontendbook.ui.profile

import android.util.Log
import androidx.lifecycle.*
import com.example.frontendbook.data.api.dto.UserResponse
import com.example.frontendbook.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val TAG = "UserViewModel"

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    private val _followersCount = MutableLiveData<Int>()
    val followersCount: LiveData<Int> = _followersCount

    private val _followingCount = MutableLiveData<Int>()
    val followingCount: LiveData<Int> = _followingCount

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadUser(userId: Long) {
        Log.d(TAG, "loadUser() -> başlıyor, userId=$userId")
        viewModelScope.launch {
            try {
                val fetched = repository.fetchUser(userId)
                Log.d(TAG, "loadUser() -> kullanıcı verisi: $fetched")
                _user.value = fetched

                val fCount = repository.getFollowersCount(userId)
                Log.d(TAG, "loadUser() -> followersCount: $fCount")
                _followersCount.value = fCount

                val gCount = repository.getFollowingCount(userId)
                Log.d(TAG, "loadUser() -> followingCount: $gCount")
                _followingCount.value = gCount

            } catch (e: Exception) {
                Log.e(TAG, "loadUser() -> hata", e)
                _error.value = e.message ?: "Kullanıcı bilgisi alınırken hata oluştu"
            }
        }
    }
}
