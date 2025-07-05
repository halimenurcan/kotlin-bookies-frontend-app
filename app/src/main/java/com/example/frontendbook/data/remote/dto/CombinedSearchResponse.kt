package com.example.frontendbook.data.remote.dto


data class CombinedSearchResponse(
    val users: List<UserDto>,
    val books: List<BookDto>
)