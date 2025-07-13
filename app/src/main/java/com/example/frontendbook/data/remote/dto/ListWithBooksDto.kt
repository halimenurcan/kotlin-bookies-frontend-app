package com.example.frontendbook.data.remote.dto

import com.example.frontendbook.data.api.dto.BookDto

data class ListWithBooksDto(
    val id: Long,
    val name: String,
    val books: List<BookDto>
)