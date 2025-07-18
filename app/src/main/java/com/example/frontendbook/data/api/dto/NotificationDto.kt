package com.example.frontendbook.data.api.dto

data class NotificationDto(
    val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val type: String,
    val targetId: String,
    val read: Boolean,
    val createdAt: String,
    val updatedAt: String
)