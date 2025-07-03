package com.example.frontendbook.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificationDto(
    val id: Long,
    @SerializedName("content") val message: String,
    @SerializedName("isRead") val read: Boolean,
    @SerializedName("timestamp") val timestamp: String
)
