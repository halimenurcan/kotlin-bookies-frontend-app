package com.example.frontendbook.data.model

import com.example.frontendbook.domain.model.Book

data class StatusBook(
    val id: Long,
    val bookId: Long,
    val userId: Long,
    val status: String,    // "READ" veya "WILL_READ"
    val createdAt: String,
    val book: Book?        // Kitap objesini de döndürüyor olabilir
)
