package com.example.frontendbook.ui.signIn

import com.example.frontendbook.ui.base.BaseUiState

sealed class SignInState : BaseUiState {
    object Loading : SignInState()
    data class Success(val token: String, val userId: Long) : SignInState()
    data class Error(val message: String) : SignInState()
}
