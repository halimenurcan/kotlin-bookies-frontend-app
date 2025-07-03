package com.example.frontendbook.data.model

data class ReadEntry(
    val id: Long? = null,
    val userId: Long,
    val bookId: String,
    val bookTitle: String,
    val bookCoverUrl: String
)

