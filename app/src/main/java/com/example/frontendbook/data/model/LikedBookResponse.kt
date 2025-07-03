package com.example.frontendbook.data.model

/**
 * GET /api/liked-books/user/{userId}/book/{bookId}
 */
data class LikedBookResponse(
    val id: Long,
    val userId: Long,
    val bookId: Long
)
