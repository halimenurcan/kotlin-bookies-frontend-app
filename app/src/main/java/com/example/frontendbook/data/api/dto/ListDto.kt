package com.example.frontendbook.data.model

import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.domain.model.Book


/**
 * Backend’den /api/lists/user/{userId} ile dönecek JSON nesnesi
 */
data class ListDto(
    val id: Long,
    val title: String,
    val description: String?,
    val books: List<Book> // her listenin kitapları

)
