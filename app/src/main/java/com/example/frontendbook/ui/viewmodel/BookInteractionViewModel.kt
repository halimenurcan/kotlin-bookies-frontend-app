package com.example.frontendbook.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.data.model.BookInteractionRequest
import com.example.frontendbook.data.repository.UserRepository
import kotlinx.coroutines.launch

class BookInteractionViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _interactionResult = MutableLiveData<Boolean>()
    val interactionResult: LiveData<Boolean> get() = _interactionResult

    fun interactWithBook(userId: Long, interaction: BookInteractionRequest) {
        viewModelScope.launch {
            try {
                val result = userRepository.sendBookInteraction(userId, interaction)
                _interactionResult.postValue(result)
            } catch (e: Exception) {
                _interactionResult.postValue(false)
            }
        }
    }
}
