package com.example.frontendbook.data.api.dto

import com.example.frontendbook.data.remote.dto.UserDto

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

    val book: BookDto? = null,
    val user:UserDto? =null

)
