package com.example.frontendbook.data.api.dto

import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.domain.model.Book


/**
 * Backend’den /api/lists/user/{userId} ile dönecek JSON nesnesi
 */
data class ListDto(
    val id: Long,
    val title: String,
    val description: String?,
    val books: List<BookDto> ,// her listenin kitapları
    val owner: OwnerDto
)


data class OwnerDto(
    val id: Long,
    val username: String
)

