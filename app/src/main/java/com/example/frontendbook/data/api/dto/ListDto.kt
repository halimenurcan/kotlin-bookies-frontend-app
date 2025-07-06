package com.example.frontendbook.data.model

import com.example.frontendbook.data.remote.dto.BookDto

/**
 * Backend’den /api/lists/user/{userId} ile dönecek JSON nesnesi
 */
data class ListDto(
    val id: Long,
    val title: String,
    val description: String?,
    val books: List<BookDto> // her listenin kitapları

)
