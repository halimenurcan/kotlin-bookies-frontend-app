package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.model.ReviewDto
import retrofit2.Response
import retrofit2.http.*

interface ReviewsApiService {
    @GET("api/comments/{id}")
    suspend fun getCommentById(@Path("id") commentId: Long): Response<ReviewDto>
    /** Tüm yorumları getirir */
    @GET("api/reviews")
    suspend fun getAllReviews(): Response<List<ReviewDto>>
    @DELETE("api/comments/{id}")
    suspend fun deleteComment(@Path("id") commentId: Long): Response<Unit>
    /** Belirli bir kitaba ait yorumları getirir */
    @GET("api/books/{bookId}/reviews")
    suspend fun getReviewsForBook(
        @Path("bookId") bookId: Long
    ): Response<List<ReviewDto>>
    @POST("api/comments")
    suspend fun createComment(
        @Body request: ReviewCreateRequest
    ): Response<ReviewDto>
}
