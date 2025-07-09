package com.example.frontendbook.data.api.dto

/**
 * Backend’den /api/comments veya benzer endpoint’ten dönen DTO
 */
data class ReviewDto(
    val id: Long,
    val userId: Long?,
    val bookId: Long,
    val bookCoverUrl: String,
    val userName: String,
    val score: Int?,
    val comment: String,
    val createdAt: String,
    var isLiked: Boolean,



)
