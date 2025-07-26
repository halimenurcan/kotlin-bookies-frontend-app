package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.LikedReviewsApiService
import com.example.frontendbook.data.model.LikedReviewRequest

class LikedReviewsRepository(
    private val api: LikedReviewsApiService
) {
    suspend fun isLiked(userId: Long, reviewId: Long): Boolean {
        val resp = api.isReviewLiked(userId, reviewId)
        return resp.isSuccessful && resp.body() == true
    }
    suspend fun getLikedReviewIds(userId: Long): List<Long> {
        val response = api.getLikedReviews(userId)
        return response.embedded.comments.map { it.id }

    }  suspend fun getLikeCount(reviewId: Long): Int {
        val resp = api.getReviewLikeCount(reviewId)
        return if (resp.isSuccessful) resp.body() ?: 0 else 0
    }

    suspend fun like(userId: Long, reviewId: Long): Boolean {
        val req = LikedReviewRequest(userId, reviewId)
        val resp = api.likeReview(req)
        return resp.isSuccessful
    }

    suspend fun unlike(userId: Long, reviewId: Long): Boolean {
        val resp = api.unlikeReview(userId, reviewId)
        return resp.isSuccessful
    }
}
