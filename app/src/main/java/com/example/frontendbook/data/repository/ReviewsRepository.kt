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
            throw Exception("Book reviews could not be loaded: ${resp.code()}")
        }

        val wrapper = resp.body()
        val dtoList = wrapper?.embedded?.comments

        if (dtoList.isNullOrEmpty()) {
            throw Exception("No reviews found for this book.")
        }

        return dtoList.map { dto ->
            ReviewDto(
                id = dto.id,
                userId = dto.user.id,
                bookId = dto.book.id,
                score = dto.score,
                comment = dto.content,
                createdAt = dto.createdAt.orEmpty(),
                bookCoverUrl = dto.book.coverImageUrl.toString(),
                userName = dto.user.username.toString(),
                isLiked = false,
                book = dto.book
            )
        }
    }

    suspend fun fetchAllComments(): List<ReviewDto> {
        val resp = api.getAllReviews()
        if (!resp.isSuccessful) {
            throw Exception("Could not be loaded: ${resp.code()}")
        }

        val wrapper = resp.body()!!
        val dtoList = wrapper.embedded.comments

        return dtoList.map { dto ->
            ReviewDto(
                id = dto.id,
                userId = dto.user.id,
                bookId = dto.book.id,
                score = dto.score,
                comment = dto.content,
                createdAt = dto.createdAt.orEmpty(),
                bookCoverUrl = dto.book.coverImageUrl.toString(),
                userName = dto.user.username.toString(),
                isLiked = false,
                book = dto.book
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

        val dto = resp.body()!!
        return ReviewDto(
            id = dto.id,
            userId = dto.user.id,
            bookId = dto.book.id,
            score = dto.score,
            comment = dto.content,
            createdAt = dto.createdAt.orEmpty(),
            bookCoverUrl = dto.book.coverImageUrl.toString(),
            userName = dto.user.username.toString(),
            isLiked = false,
            book = dto.book
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

        val dto = resp.body()!!
        return ReviewDto(
            id = dto.id,
            userId = dto.user.id,
            bookId = dto.book.id,
            score = dto.score,
            comment = dto.content,
            createdAt = dto.createdAt ?: "",
            bookCoverUrl = dto.book.coverImageUrl.toString(),
            userName = dto.user.username.toString(),
            isLiked = false,
            book = dto.book
        )
    }
}
