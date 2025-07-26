package com.example.frontendbook.data.model

import com.example.frontendbook.R

data class Notification(
    val id: Long,
    val message: String,
    val time: String,
    val type: NotificationType,
    val relatedId: String,
    val senderUsername: String,
    val read: Boolean
)

enum class NotificationType {
    FOLLOW,
    LIKE_COMMENT,
    FOLLOW_LIST;

    fun getIconRes(): Int {
        return when (this) {
            FOLLOW       -> R.drawable.notificationsfollow
            LIKE_COMMENT -> R.drawable.notificationslike
            FOLLOW_LIST  -> R.drawable.notificationslikelists
        }
    }

    fun getTitle(): String {
        return when (this) {
            FOLLOW       -> "You have a new follower!"
            LIKE_COMMENT -> "Your comment was liked!"
            FOLLOW_LIST  -> "Your list received a like!"
        }
    }
}

