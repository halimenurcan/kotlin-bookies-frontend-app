package com.example.frontendbook.data.model

import com.example.frontendbook.data.api.dto.UserResponse

/**
 * GET /api/followers?userId=&followerId=
 */
data class FollowersResponse(
    val _embedded: EmbeddedUsers
)
data class EmbeddedUsers(
    val userResponseDTOList: List<UserResponse>
)
