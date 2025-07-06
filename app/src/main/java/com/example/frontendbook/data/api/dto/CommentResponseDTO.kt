package com.example.frontendbook.data.api.dto

import com.example.frontendbook.data.remote.dto.UserDto

data class CommentResponseDTO(
    val id: Long,
    val content: String,
    val score: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val book: BookDto,
    val user: UserDto
)