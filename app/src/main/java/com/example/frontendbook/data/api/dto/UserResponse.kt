package com.example.frontendbook.data.api.dto

data class UserResponse(
    val id: Long,
    val username: String,
    val fullName: String?,
    val profileImageUrl: String?

)