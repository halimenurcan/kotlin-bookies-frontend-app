package com.example.frontendbook.data.model

/**
 * POST /api/followers
 */
data class FollowerRequest(
    val userId: Long,
    val followerId: Long
)
