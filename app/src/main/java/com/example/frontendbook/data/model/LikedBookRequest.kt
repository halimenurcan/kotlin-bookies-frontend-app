package com.example.frontendbook.data.model


/**
 * POST /api/liked-books
 */
data class LikedBookRequest(
    val userId: Long,
    val bookId: Long
)
