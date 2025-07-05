package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.ReviewsApiService
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.model.ReviewDto

class ReviewsRepository(
    private val api: ReviewsApiService
) {
    suspend fun fetchAllReviews(): List<ReviewDto> {
        val resp = api.getAllReviews()
        if (resp.isSuccessful) return resp.body().orEmpty()
        throw Exception("Yorumlar yüklenemedi: ${resp.code()}")
    }
    suspend fun deleteReview(id: Long) {
        val resp = api.deleteComment(id)
        if (!resp.isSuccessful) throw Exception("Yorum silme hatası: ${resp.code()}")
    }

    suspend fun fetchReviewsForBook(bookId: Long): List<ReviewDto> {
        val resp = api.getReviewsForBook(bookId)
        if (resp.isSuccessful) return resp.body().orEmpty()
        throw Exception("Kitap yorumları yüklenemedi: ${resp.code()}")
    }
    suspend fun getCommentById(id: Long): ReviewDto {
        val resp = api.getCommentById(id)
        if (resp.isSuccessful) return resp.body()!!
        throw Exception("Yorum getirilemedi: ${resp.code()}")
    }
    suspend fun createComment(request: ReviewCreateRequest): ReviewDto {
        val resp = api.createComment(request)
        if (resp.isSuccessful) return resp.body()!!
        throw Exception("Yorum oluşturulamadı: ${resp.code()}")
    }
}
