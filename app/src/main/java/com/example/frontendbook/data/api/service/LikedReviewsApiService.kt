package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.LikedReviewDto
import com.example.frontendbook.data.api.dto.LikedReviewResponseWrapper
import com.example.frontendbook.data.api.dto.LikedReviewsResponse
import retrofit2.Response
import retrofit2.http.*

interface LikedReviewsApiService {

    @GET("liked-comments/user/{userId}/comment/{reviewId}/is-liked")
    suspend fun isReviewLiked(
        @Path("userId") userId: Long,
        @Path("commentId") reviewId: Long
    ): Response<Boolean>

    @GET("liked-comments/comment/{reviewId}/count")
    suspend fun getReviewLikeCount(
        @Path("commentId") reviewId: Long
    ): Response<Int>

    @POST("liked-comments")
    suspend fun likeReview(
        @Body req: com.example.frontendbook.data.model.LikedReviewRequest
    ): Response<Unit>

    @DELETE("liked-comments/user/{userId}/comment/{commentId}")
    suspend fun unlikeReview(
        @Path("userId") userId: Long,
        @Path("commentId") commentId: Long
    ): Response<Unit>

    @GET("liked-comments/{userId}")
    suspend fun getLikedReviews(@Path("userId") userId: Long): LikedReviewsResponse
}

