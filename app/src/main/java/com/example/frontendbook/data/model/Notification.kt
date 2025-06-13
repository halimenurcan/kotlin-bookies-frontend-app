package com.example.frontendbook.data.model

data class Notification(
    val iconResId: Int,
    val message: String,
    val time: String,
    val type: NotificationType,
    val relatedId: String
)

//bildirim türünü belirlemek için bildirim tipi enum class oluşturduk
enum class NotificationType {
    FOLLOW,
    LIKE_COMMENT,
    FOLLOW_LIST
}

