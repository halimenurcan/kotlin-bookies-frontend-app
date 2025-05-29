package com.example.frontendbook.data.remote.dto

data class MessageResponse(
    val data: List<MessageData>
)

data class MessageData(
    val content: List<Content>
)

data class Content(
    val text: Text
)

data class Text(
    val value: String
)
