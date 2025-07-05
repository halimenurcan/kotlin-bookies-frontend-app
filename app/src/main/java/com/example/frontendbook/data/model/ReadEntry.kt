package com.example.frontendbook.data.model

data class ReadEntry(
    val id: Long,
    val bookId: String,
    val bookTitle: String,
    val bookAuthor: String?,
    val bookIsbn: String?,
    val bookDescription: String?,
    val bookPageCount: Int?,
    val bookPublisher: String?,
    val bookPublishedYear: Int?,
    val bookCoverUrl: String,
    val userId: Long,
    val rating : Int
)
