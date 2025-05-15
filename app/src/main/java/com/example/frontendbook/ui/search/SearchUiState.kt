package com.example.frontendbook.ui.search

import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.base.BaseUiState

data class SearchUiState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val selectedYear: Int? = null,
    val selectedCategory: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isEmptyResult: Boolean = false
) : BaseUiState
