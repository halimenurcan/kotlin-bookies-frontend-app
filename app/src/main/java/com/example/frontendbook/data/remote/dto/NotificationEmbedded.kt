package com.example.frontendbook.data.remote.dto

import com.example.frontendbook.data.api.dto.NotificationDto
import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("_embedded")
    val embedded: NotificationEmbedded?
)

data class NotificationEmbedded(
    @SerializedName("notificationResponseDTOList")
    val notifications: List<NotificationDto>
)

