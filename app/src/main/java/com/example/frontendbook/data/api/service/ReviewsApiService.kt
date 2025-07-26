package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.CommentResponseDTO
import com.example.frontendbook.data.api.dto.CommentsResponse
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.model.ReviewCreateRequest
import retrofit2.Response
import retrofit2.http.*

interface ReviewsApiService {


    @GET("comments/{id}")
    suspend fun getCommentById(
        @Path("id") commentId: Long
    ): Response<CommentResponseDTO>

    @GET("comments")
    suspend fun getAllReviews(): Response<CommentsResponse>

    @DELETE("comments/{id}")
    suspend fun deleteComment(
        @Path("id") commentId: Long
    ): Response<Unit>

    @GET("comments/book/{bookId}")
    suspend fun getReviewsForBook(
        @Path("bookId") bookId: Long
    ): Response<CommentsResponse>

    @POST("comments")
    suspend fun createComment(
        @Body request: ReviewCreateRequest
    ): Response<CommentResponseDTO>


}
