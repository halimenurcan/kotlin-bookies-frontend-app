package com.example.frontendbook.data.repository

import android.util.Log
import com.example.frontendbook.data.api.dto.CommentResponseDTO
import com.example.frontendbook.data.api.dto.CommentsResponse
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.api.service.ReviewsApiService
import com.example.frontendbook.data.model.ReviewCreateRequest

class ReviewsRepository(
    private val api: ReviewsApiService
) {

    suspend fun fetchAllComments(): List<ReviewDto> {
        val resp = api.getAllReviews()
        if (!resp.isSuccessful) {
            throw Exception("Yüklenemedi: ${resp.code()}")
        }

        // HAL envelope
        val wrapper = resp.body()!!                      // CommentsResponse
        val dtoList = wrapper.embedded.comments         // List<CommentResponseDTO>

        return dtoList.map { dto ->
            ReviewDto(
                id           = dto.id,
                userId       = dto.user.id,
                bookId       = dto.book.id,
                score        = dto.score,
                comment      = dto.content,
                createdAt    = dto.createdAt.orEmpty(),
                bookCoverUrl = dto.book.coverImageUrl.toString(),
                userName     = dto.user.username.toString()
            )
        }
    }

    /** Belirli bir yorumu siler */
    suspend fun deleteReview(id: Long) {
        Log.d("ReviewsRepo", "→ deleteReview(id=$id) called")
        val resp = api.deleteComment(id)
        Log.d(
            "ReviewsRepo", "← deleteReview response: code=${resp.code()}, url=${resp.raw().request.url}"
        )
        if (!resp.isSuccessful) {
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
            "ReviewsRepo",
            "← fetchReviewsForBook response: code=${resp.code()}, url=${resp.raw().request.url}"
        )

        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! fetchReviewsForBook failed: body=$err")
            throw Exception("Kitap yorumları yüklenemedi: ${resp.code()}")
        }

        val dtoList: List<CommentResponseDTO> = resp.body().orEmpty()
        return dtoList.map { dto ->
            ReviewDto(
                id           = dto.id,
                userId       = dto.user.id,
                bookId       = dto.book.id,
                score        = dto.score,
                comment      = dto.content,
                createdAt    = dto.createdAt.orEmpty(),
                bookCoverUrl = dto.book.coverImageUrl.toString(),
                userName     = dto.user.username.toString()
            )
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
            val dto = resp.body()!!  // CommentResponseDTO
            val review = ReviewDto(
                id           = dto.id,
                userId       = dto.user.id,
                bookId       = dto.book.id,
                score        = dto.score,
                comment      = dto.content,
                createdAt    = dto.createdAt.orEmpty(),
                bookCoverUrl = dto.book.coverImageUrl.toString(),
                userName     = dto.user.username.toString()
            )
            return review
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
            "ReviewsRepo",
            "← createComment response: code=${resp.code()}, url=${resp.raw().request.url}"
        )

        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! createComment failed: body=$err")
            throw Exception("Yorum oluşturulamadı: ${resp.code()}")
        }

        val dto = resp.body()!!  // CommentResponseDTO
        val review = ReviewDto(
            id           = dto.id,
            userId       = dto.user.id,
            bookId       = dto.book.id,
            score        = dto.score,
            comment      = dto.content,
            createdAt    = dto.createdAt ?: "",
            bookCoverUrl = dto.book.coverImageUrl.toString(),
            userName     = dto.user.username.toString()
        )
        return review
    }
}
