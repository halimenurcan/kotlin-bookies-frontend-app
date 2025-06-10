package com.example.frontendbook.domain.model

data class UserSimple(
    val userId: String,
    val username: String,
    val avatarUrl: String? = null
)
