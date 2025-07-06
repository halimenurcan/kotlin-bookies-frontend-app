package com.example.frontendbook.data.model

/**
 * POST /api/followers
 */
data class FollowerRequest(
    val followerId: Long,
    val followedId:Long
)
