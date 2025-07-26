package com.example.frontendbook.ui.profile

import android.util.Log
import androidx.lifecycle.*
import com.example.frontendbook.data.api.dto.UserResponse
import com.example.frontendbook.data.repository.UserRepository
import com.example.frontendbook.data.repository.FollowersRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository,
    private val followersRepository: FollowersRepository // <-- EKLENDİ
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

                // (İsteğe bağlı: burada sayıları da yükleyebilirsin)
                loadFollowersCount(userId)
                loadFollowingCount(userId)
            } catch (e: Exception) {
                Log.e(TAG, "loadUser() -> hata", e)
                _error.value = e.message ?: "Kullanıcı bilgisi alınırken hata oluştu"
            }
        }
    }

    fun loadFollowersCount(userId: Long) {
        viewModelScope.launch {
            try {
                val count = followersRepository.getFollowerCount(userId)
                _followersCount.value = count
                Log.d(TAG, "loadFollowersCount() -> $count")
            } catch (e: Exception) {
                Log.e(TAG, "loadFollowersCount() -> hata", e)
                _error.value = e.message ?: "Takipçi sayısı alınamadı"
            }
        }
    }

    fun loadFollowingCount(userId: Long) {
        viewModelScope.launch {
            try {
                val count = followersRepository.getFollowingCount(userId)
                _followingCount.value = count
                Log.d(TAG, "loadFollowingCount() -> $count")
            } catch (e: Exception) {
                Log.e(TAG, "loadFollowingCount() -> hata", e)
                _error.value = e.message ?: "Takip edilen sayısı alınamadı"
            }
        }
    }

    fun updateAvatar(userId: Long, avatarId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.updateAvatar(userId, avatarId)
                Log.d("UserViewModel", "Avatar güncellendi: $response")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Avatar güncelleme hatası", e)
            }
        }
    }




}
