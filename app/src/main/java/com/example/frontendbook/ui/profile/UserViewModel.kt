package com.example.frontendbook.ui.profile

import androidx.lifecycle.*
import com.example.frontendbook.data.api.dto.UserResponse
import com.example.frontendbook.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _user  = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    /**
     * Backend'den kullanıcı bilgilerini çeker.
     * @param userId: çekilecek kullanıcının ID'si
     */
    fun loadUser(userId: Long) {
        viewModelScope.launch {
            try {
                val fetched = repository.fetchUser(userId)
                _user.value = fetched
            } catch (e: Exception) {
                _error.value = e.message ?: "Kullanıcı bilgisi alınırken hata oluştu"
            }
        }
    }
}
