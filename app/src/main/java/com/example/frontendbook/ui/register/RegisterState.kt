package com.example.frontendbook.ui.register

import com.example.frontendbook.ui.base.BaseUiState

sealed class RegisterState : BaseUiState {
    object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Error(val errorMessage: String) : RegisterState()
}
