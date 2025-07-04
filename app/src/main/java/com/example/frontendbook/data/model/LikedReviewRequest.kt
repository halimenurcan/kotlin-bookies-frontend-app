package com.example.frontendbook.data.model

/**
 * POST /api/liked-comments
 */
data class LikedReviewRequest(
    val userId: Long,
    val commentId: Long
)
