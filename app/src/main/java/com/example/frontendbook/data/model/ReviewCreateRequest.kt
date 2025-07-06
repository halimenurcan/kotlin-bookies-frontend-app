package com.example.frontendbook.data.model

data class ReviewCreateRequest (
    val userId: Long,
    val bookId: Long,
    val score: Int,
    val comment: String,
    val read: Boolean,
    val toRead: Boolean,
    val liked: Boolean
)