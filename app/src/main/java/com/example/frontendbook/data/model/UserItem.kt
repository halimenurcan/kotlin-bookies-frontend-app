package com.example.frontendbook.domain.model

data class UserItem(
    val userId: String,
    val username: String,
    val profileImageUrl: String? = null
)