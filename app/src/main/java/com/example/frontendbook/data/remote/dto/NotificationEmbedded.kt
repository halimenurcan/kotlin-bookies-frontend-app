package com.example.frontendbook.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificationEmbedded(
    @SerializedName("notifications")
    val notifications: List<NotificationDto>
)

data class NotificationResponse(
    @SerializedName("_embedded")
    val embedded: NotificationEmbedded
)
