package com.example.frontendbook.data.api.service

import retrofit2.Response
import retrofit2.http.*

interface LikedReviewsApiService {

    /** Kullanıcının bu incelemeyi beğenip beğenmediğini kontrol et */
    @GET("liked-comments/user/{userId}/comment/{reviewId}/is-liked")
    suspend fun isReviewLiked(
        @Path("userId") userId: Long,
        @Path("commentId") reviewId: Long
    ): Response<Boolean>

    /** Bir incelemenin toplam beğeni sayısını al */
    @GET("liked-comments/comment/{reviewId}/count")
    suspend fun getReviewLikeCount(
        @Path("commentId") reviewId: Long
    ): Response<Int>

    /** İnceleme beğenisi oluştur (POST /api/liked-comments) */
    @POST("liked-comments")
    suspend fun likeReview(
        @Body req: com.example.frontendbook.data.model.LikedReviewRequest
    ): Response<Unit>

    /** İnceleme beğenisini sil (unlike) */
    @DELETE("liked-comments/user/{userId}/comment/{reviewId}")
    suspend fun unlikeReview(
        @Path("userId") userId: Long,
        @Path("commentId") reviewId: Long
    ): Response<Unit>
}
