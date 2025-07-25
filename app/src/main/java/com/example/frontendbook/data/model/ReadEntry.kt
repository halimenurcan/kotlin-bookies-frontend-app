package com.example.frontendbook.data.model

data class ReadEntry(
    val bookId: Long,
    val bookTitle: String,
    val bookAuthor: String?,
    val bookIsbn: String?,
    val bookDescription: String?,
    val bookPageCount: Int?,
    val bookPublisher: String?,
    val bookPublishedYear: Int?,
    val bookCoverUrl: String,
    val userId: Long,
)
