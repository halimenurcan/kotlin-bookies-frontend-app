package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.model.ReviewDto
import retrofit2.Response
import retrofit2.http.*

interface ReviewsApiService {

    /** Tüm yorumları getirir */
    @GET("api/reviews")
    suspend fun getAllReviews(): Response<List<ReviewDto>>

    /** Belirli bir kitaba ait yorumları getirir */
    @GET("api/books/{bookId}/reviews")
    suspend fun getReviewsForBook(
        @Path("bookId") bookId: Long
    ): Response<List<ReviewDto>>
}
