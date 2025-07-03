package com.example.frontendbook.data.model

/**
 * POST /api/liked-comments
 */
data class LikedCommentRequest(
    val userId: Long,
    val commentId: Long
)
