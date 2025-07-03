package com.example.frontendbook.data.model

/**
 * Backend’den /api/lists/user/{userId} ile dönecek JSON nesnesi
 */
data class ListDto(
    val id: Long,
    val title: String,
    val description: String?
)
