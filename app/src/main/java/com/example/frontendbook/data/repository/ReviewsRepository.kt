package com.example.frontendbook.data.repository

import android.util.Log
import com.example.frontendbook.data.api.service.ReviewsApiService
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.model.ReviewDto

/**
 * ReviewsApiService çağrılarını loglayarak sarar.
 */
class ReviewsRepository(
    private val api: ReviewsApiService
) {

    /** Tüm yorumları getirir */
    suspend fun fetchAllReviews(): List<ReviewDto> {
        Log.d("ReviewsRepo", "→ fetchAllReviews() called")
        val resp = api.getAllReviews()
        Log.d(
            "ReviewsRepo", "← fetchAllReviews response: code=${resp.code()}, url=${resp.raw().request.url}"
        )
        if (resp.isSuccessful) {
            val list = resp.body().orEmpty()
            Log.d("ReviewsRepo", "✓ fetchAllReviews successful: items=${list.size}")
            return list
        } else {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! fetchAllReviews failed: body=$err")
            throw Exception("Yorumlar yüklenemedi: ${resp.code()}")
        }
    }

    /** Belirli bir yorumu siler */
    suspend fun deleteReview(id: Long) {
        Log.d("ReviewsRepo", "→ deleteReview(id=$id) called")
        val resp = api.deleteComment(id)
        Log.d(
            "ReviewsRepo", "← deleteReview response: code=${resp.code()}, url=${resp.raw().request.url}"
        )
        if (resp.isSuccessful) {
            Log.d("ReviewsRepo", "✓ deleteReview successful")
        } else {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! deleteReview failed: body=$err")
            throw Exception("Yorum silme hatası: ${resp.code()}")
        }
    }

    /** Bir kitaba ait yorumları getirir */
    suspend fun fetchReviewsForBook(bookId: Long): List<ReviewDto> {
        Log.d("ReviewsRepo", "→ fetchReviewsForBook(bookId=$bookId) called")
        val resp = api.getReviewsForBook(bookId)
        Log.d(
            "ReviewsRepo", "← fetchReviewsForBook response: code=${resp.code()}, url=${resp.raw().request.url}"
        )
        if (resp.isSuccessful) {
            val list = resp.body().orEmpty()
            Log.d("ReviewsRepo", "✓ fetchReviewsForBook successful: items=${list.size}")
            return list
        } else {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! fetchReviewsForBook failed: body=$err")
            throw Exception("Kitap yorumları yüklenemedi: ${resp.code()}")
        }
    }

    /** Tek bir yorumu getirir */
    suspend fun getCommentById(id: Long): ReviewDto {
        Log.d("ReviewsRepo", "→ getCommentById(id=$id) called")
        val resp = api.getCommentById(id)
        Log.d(
            "ReviewsRepo", "← getCommentById response: code=${resp.code()}, url=${resp.raw().request.url}"
        )
        if (resp.isSuccessful) {
            val comment = resp.body()!!
            Log.d("ReviewsRepo", "✓ getCommentById successful: body=$comment")
            return comment
        } else {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! getCommentById failed: body=$err")
            throw Exception("Yorum getirilemedi: ${resp.code()}")
        }
    }

    /** Yeni yorum oluşturur */
    suspend fun createComment(request: ReviewCreateRequest): ReviewDto {
        Log.d("ReviewsRepo", "→ createComment(request=$request) called")
        val resp = api.createComment(request)
        Log.d(
            "ReviewsRepo", "← createComment response: code=${resp.code()}, url=${resp.raw().request.url}"
        )
        if (resp.isSuccessful) {
            val created = resp.body()!!
            Log.d("ReviewsRepo", "✓ createComment successful: body=$created")
            return created
        } else {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! createComment failed: body=$err")
            throw Exception("Yorum oluşturulamadı: ${resp.code()}")
        }
    }
}
