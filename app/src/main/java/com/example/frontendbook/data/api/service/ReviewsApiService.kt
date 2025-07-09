package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.CommentResponseDTO
import com.example.frontendbook.data.api.dto.CommentsResponse
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.model.ReviewCreateRequest
import retrofit2.Response
import retrofit2.http.*

interface ReviewsApiService {

    /** Tek bir yorumu HAL‐sarmalayıcı CommentResponseDTO olarak döner */
    @GET("comments/{id}")
    suspend fun getCommentById(
        @Path("id") commentId: Long
    ): Response<CommentResponseDTO>

    /** Tüm yorumları HAL‐sarmalayıcı CommentsResponse ile döner */
    @GET("comments")
    suspend fun getAllReviews(): Response<CommentsResponse>

    /** Yorum silme */
    @DELETE("comments/{id}")
    suspend fun deleteComment(
        @Path("id") commentId: Long
    ): Response<Unit>

    /** Bir kitaba ait yorumları düz liste olarak döner (ReviewDto shape’inde) */
    @GET("comments/book/{bookId}")
    suspend fun getReviewsForBook(
        @Path("bookId") bookId: Long
    ): Response<CommentsResponse>

    /** Yeni yorum yaratır, geriye HAL‐DTO olarak CommentResponseDTO döner */
    @POST("comments")
    suspend fun createComment(
        @Body request: ReviewCreateRequest
    ): Response<CommentResponseDTO>


}
