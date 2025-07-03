package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.ReviewsApiService
import com.example.frontendbook.data.model.ReviewDto

class ReviewsRepository(
    private val api: ReviewsApiService
) {
    suspend fun fetchAllReviews(): List<ReviewDto> {
        val resp = api.getAllReviews()
        if (resp.isSuccessful) return resp.body().orEmpty()
        throw Exception("Yorumlar yüklenemedi: ${resp.code()}")
    }

    suspend fun fetchReviewsForBook(bookId: Long): List<ReviewDto> {
        val resp = api.getReviewsForBook(bookId)
        if (resp.isSuccessful) return resp.body().orEmpty()
        throw Exception("Kitap yorumları yüklenemedi: ${resp.code()}")
    }
}
