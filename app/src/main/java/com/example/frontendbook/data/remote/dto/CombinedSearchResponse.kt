package com.example.frontendbook.data.remote.dto

import com.example.frontendbook.data.api.dto.BookDto


data class CombinedSearchResponse(
    val users: List<UserDto>,
    val books: List<BookDto>
)