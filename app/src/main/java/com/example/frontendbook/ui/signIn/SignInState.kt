package com.example.frontendbook.ui.signIn

sealed class SignInState {
    object Loading : SignInState()
    data class Success(val token: String?) : SignInState()
    data class Error(val message: String) : SignInState()
}
