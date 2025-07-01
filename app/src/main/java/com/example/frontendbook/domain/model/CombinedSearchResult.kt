package com.example.frontendbook.domain.model

sealed class CombinedSearchResult {
    data class BookResult(val book: Book) : CombinedSearchResult()
    data class UserResult(val user: User) : CombinedSearchResult()
}
