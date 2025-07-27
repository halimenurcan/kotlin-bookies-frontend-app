package com.example.frontendbook.data.api.dto

import com.google.gson.annotations.SerializedName

data class EmbeddedComments(
    @SerializedName("commentResponseDTOList")
    val comments: List<ReviewDto> = listOf()
)
