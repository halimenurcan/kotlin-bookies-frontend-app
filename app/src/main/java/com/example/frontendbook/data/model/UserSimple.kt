package com.example.frontendbook.data.model

import com.example.frontendbook.data.api.dto.UserResponse

data class UserSimple(
    val userId: String,
    val username: String,
    val avatarUrl: String? = null
)
fun UserResponse.toSimple(): UserSimple = UserSimple(
    userId = this.id.toString(),
    username = this.username,
    avatarUrl = this.profileImageUrl
)