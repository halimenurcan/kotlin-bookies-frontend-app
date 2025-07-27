package com.example.frontendbook.data.api.dto
data class AvatarResponse(
    val id: Long,
    val createdAt: String,
    val updatedAt: String,
    val userId: Long,
    val avatar: String
)
