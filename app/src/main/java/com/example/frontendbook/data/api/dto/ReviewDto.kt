package com.example.frontendbook.data.model

/**
 * Backend’den /api/reviews veya benzer endpoint’ten dönen DTO
 */
data class ReviewDto(
    val id: Long,
    val userId: Long,
    val bookId: Long,
    val score: Int,
    val comment: String,
    val createdAt: String
)
