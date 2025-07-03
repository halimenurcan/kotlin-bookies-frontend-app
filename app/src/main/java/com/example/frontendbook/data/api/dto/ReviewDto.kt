package com.example.frontendbook.data.model

/**
 * Backend’den /api/reviews veya benzer endpoint’ten dönen DTO
 */
data class ReviewDto(
    val id: Long,
    val userId: Long,
    val username: String,
    val content: String,
    val rating: Float,
    val timestamp: String
)
