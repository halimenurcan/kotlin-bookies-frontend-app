package com.example.frontendbook.data.api.dto

import com.google.gson.annotations.SerializedName

data class LikedReviewResponseWrapper(
    @SerializedName("_embedded")
    val embedded: Embedded
)

data class Embedded(
    @SerializedName("commentResponseDTOList")
    val commentList: List<ReviewDto>
)
data class LikedReviewsResponse(
    @SerializedName("_embedded")
    val embedded: EmbeddedLikedReviews?
)

data class EmbeddedLikedReviews(
    @SerializedName("commentResponseDTOList")
    val comments: List<LikedReviewDto> = emptyList() 
)