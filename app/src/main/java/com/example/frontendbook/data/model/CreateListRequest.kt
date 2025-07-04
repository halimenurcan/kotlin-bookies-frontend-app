package com.example.frontendbook.data.model

data class CreateListRequest(
    val userId: Long,
    val title: String,
    val description: String? = null
)
