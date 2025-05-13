package com.example.frontendbook.ui.register

sealed class RegisterState {
    object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Error(val errorMessage: String) : RegisterState()
}
