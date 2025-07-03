package com.example.frontendbook.data.model

data class UserProfileResponse(
    val id: Long,
    val username: String,
    val followersCount: CountWrapper,
    val followingCount: CountWrapper
)

data class CountWrapper(
    val value: Int
)
