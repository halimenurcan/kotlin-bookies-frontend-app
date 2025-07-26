package com.example.frontendbook.data.api.dto

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

    val book: BookDto? = null


)
