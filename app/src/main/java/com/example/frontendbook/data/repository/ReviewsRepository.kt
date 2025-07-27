package com.example.frontendbook.data.repository

import android.util.Log
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.api.service.ReviewsApiService
import com.example.frontendbook.data.model.ReviewCreateRequest

class ReviewsRepository(
    private val api: ReviewsApiService
) {

    suspend fun fetchReviewsForBook(bookId: Long): List<ReviewDto> {
        Log.d("ReviewsRepo", "→ fetchReviewsForBook(bookId=$bookId) called")
        val resp = api.getReviewsForBook(bookId)
        Log.d("ReviewsRepo", "← fetchReviewsForBook response: code=${resp.code()}, url=${resp.raw().request.url}")

        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! fetchReviewsForBook failed: body=$err")
            throw Exception("Book reviews could not be loaded: ${resp.code()}")
        }

        val dtoList = resp.body()?.embedded?.comments

        if (dtoList.isNullOrEmpty()) {
            throw Exception("No reviews found for this book.")
        }

        return dtoList.mapNotNull { dto ->
            val user = dto.user
            val book = dto.book

            if (user == null || book == null) {
                Log.w("DTO_MAPPING", "Skipping review with null user or book: dto=$dto")
                return@mapNotNull null
            }

            ReviewDto(
                id = dto.id,
                userId = user.id,
                bookId = book.id,
                score = dto.score,
                comment = dto.comment ?: "",
                createdAt = dto.createdAt.orEmpty(),
                bookCoverUrl = book.coverImageUrl ?: "",
                userName = user.username ?: "Unknown",
                isLiked = false,
                book = book
            )
        }
    }

    suspend fun fetchAllComments(): List<ReviewDto> {
        val resp = api.getAllReviews()
        if (!resp.isSuccessful) {
            throw Exception("Could not be loaded: ${resp.code()}")
        }

        val dtoList = resp.body()?.embedded?.comments

        if (dtoList.isNullOrEmpty()) return emptyList()

        return dtoList.mapNotNull { dto ->
            val user = dto.user
            val book = dto.book

            if (user == null || book == null) {
                Log.w("DTO_MAPPING", "Skipping review with null user or book: dto=$dto")
                return@mapNotNull null
            }

            ReviewDto(
                id = dto.id,
                userId = user.id,
                bookId = book.id,
                score = dto.score,
                comment = dto.comment ?: "",
                createdAt = dto.createdAt.orEmpty(),
                bookCoverUrl = book.coverImageUrl ?: "",
                userName = user.username ?: "Unknown",
                isLiked = false,
                book = book
            )
        }
    }

    suspend fun deleteReview(id: Long) {
        Log.d("ReviewsRepo", "→ deleteReview(id=$id) called")
        val resp = api.deleteComment(id)
        Log.d("ReviewsRepo", "← deleteReview response: code=${resp.code()}, url=${resp.raw().request.url}")
        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! deleteReview failed: body=$err")
            throw Exception("Comment deletion error: ${resp.code()}")
        }
    }

    suspend fun getCommentById(id: Long): ReviewDto {
        Log.d("ReviewsRepo", "→ getCommentById(id=$id) called")
        val resp = api.getCommentById(id)
        Log.d("ReviewsRepo", "← getCommentById response: code=${resp.code()}, url=${resp.raw().request.url}")
        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! getCommentById failed: body=$err")
            throw Exception("No comments available: ${resp.code()}")
        }

        val dto = resp.body() ?: throw Exception("Response body is null")
        val user = dto.user
        val book = dto.book

        if (user == null || book == null) {
            throw Exception("User or Book information is missing in comment response")
        }

        return ReviewDto(
            id = dto.id,
            userId = user.id,
            bookId = book.id,
            score = dto.score,
            comment = dto.content ?: "",
            createdAt = dto.createdAt.orEmpty(),
            bookCoverUrl = book.coverImageUrl ?: "",
            userName = user.username ?: "Unknown",
            isLiked = false,
            book = book
        )
    }

    suspend fun createComment(request: ReviewCreateRequest): ReviewDto {
        Log.d("ReviewsRepo", "→ createComment(request=$request) called")
        val resp = api.createComment(request)
        Log.d("ReviewsRepo", "← createComment response: code=${resp.code()}, url=${resp.raw().request.url}")

        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            Log.e("ReviewsRepo", "!! createComment failed: body=$err")
            throw Exception("Comment could not be created: ${resp.code()}")
        }

        val dto = resp.body() ?: throw Exception("Response body is null")
        val user = dto.user
        val book = dto.book

        if (user == null || book == null) {
            throw Exception("User or Book information is missing in comment response")
        }

        return ReviewDto(
            id = dto.id,
            userId = user.id,
            bookId = book.id,
            score = dto.score,
            comment = dto.content ?: "",
            createdAt = dto.createdAt ?: "",
            bookCoverUrl = book.coverImageUrl ?: "",
            userName = user.username ?: "Unknown",
            isLiked = false,
            book = book
        )
    }
}
