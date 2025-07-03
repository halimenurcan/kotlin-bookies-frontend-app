package com.example.frontendbook.data.api.service

import retrofit2.Response
import retrofit2.http.*

interface LikedCommentsApiService {

    /** Kullanıcının bu yorumu beğenip beğenmediğini kontrol et */
    @GET("api/liked-comments/user/{userId}/comment/{commentId}/is-liked")
    suspend fun isLiked(
        @Path("userId") userId: Long,
        @Path("commentId") commentId: Long
    ): Response<Boolean>

    /** Bir yorumun toplam beğeni sayısını al */
    @GET("api/liked-comments/comment/{commentId}/count")
    suspend fun getLikeCount(
        @Path("commentId") commentId: Long
    ): Response<Int>

    /** Yorum beğenisi oluştur */
    @POST("api/liked-comments")
    suspend fun likeComment(
        @Body req: com.example.frontendbook.data.model.LikedCommentRequest
    ): Response<Unit>

    /** Yorum beğenisini sil (unlike) */
    @DELETE("api/liked-comments/user/{userId}/comment/{commentId}")
    suspend fun unlikeComment(
        @Path("userId") userId: Long,
        @Path("commentId") commentId: Long
    ): Response<Unit>
}
