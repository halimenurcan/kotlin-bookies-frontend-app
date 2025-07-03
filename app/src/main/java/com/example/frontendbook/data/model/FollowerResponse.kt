package com.example.frontendbook.data.model

/**
 * GET /api/followers?userId=&followerId=
 */
data class FollowerResponse(
    val userId: Long,
    val followerId: Long
)
