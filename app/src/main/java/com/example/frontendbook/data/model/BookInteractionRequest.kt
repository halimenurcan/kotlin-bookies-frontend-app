package com.example.frontendbook.data.model

data class BookInteractionRequest(
    val userId: Long,
    val bookId: Long,
    val read: Boolean,
    val liked: Boolean,
    val inReadList: Boolean,
    val comment: String?,
    val rating: Int
)
