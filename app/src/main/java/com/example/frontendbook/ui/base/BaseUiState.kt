package com.example.frontendbook.ui.base

interface BaseUiState {
    object Loading : BaseUiState
    data class Success(val message: String? = null) : BaseUiState
    data class Error(val errorMessage: String) : BaseUiState
}
