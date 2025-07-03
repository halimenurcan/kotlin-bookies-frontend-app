package com.example.frontendbook.data.model

data class BookInteractionRequest(
    val userId: Long,
    val bookId: Long,
    val isRead: Boolean,
    val isLiked: Boolean,
    val isInReadlist: Boolean,
    val comment: String?,
    val rating: Int
)
