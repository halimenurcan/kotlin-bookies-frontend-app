package com.example.frontendbook.data.model

/**
 * POST /api/list-follows
 * veya POST /api/list-follows/unfollow
 */
data class ListFollowRequest(
    val userId: Long,
    val listId: Long
)
