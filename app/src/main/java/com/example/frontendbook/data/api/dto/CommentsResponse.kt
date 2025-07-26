package com.example.frontendbook.data.api.dto

import com.google.gson.annotations.SerializedName

data class CommentsResponse(
    @SerializedName("_embedded")
    val embedded: EmbeddedComments
)
data class CommentResponseWrapper(
    @SerializedName("_embedded")
    val embedded: EmbeddedComments? = null
)