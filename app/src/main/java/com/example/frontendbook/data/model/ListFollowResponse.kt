package com.example.frontendbook.data.model

/**
 * GET /api/list-follows/user/{userId}/list/{listId}
 */
data class ListFollowResponse(
    val id: Long,
    val userId: Long,
    val listId: Long
)
