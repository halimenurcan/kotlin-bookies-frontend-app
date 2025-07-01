package com.example.frontendbook.domain.model

data class User(
    val id: String,
    val username: String,
    val fullName: String?,
    val profileImageUrl: String?
)
