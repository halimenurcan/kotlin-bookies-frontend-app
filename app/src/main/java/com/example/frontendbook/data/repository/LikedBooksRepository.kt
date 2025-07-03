package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.LikedBooksApiService
import com.example.frontendbook.data.model.LikedBookRequest

class LikedBooksRepository(
    private val api: LikedBooksApiService
) {

    suspend fun likeBook(userId: Long, bookId: Long): Boolean {
        val req = LikedBookRequest(userId = userId, bookId = bookId)
        return api.createLikedBook(req).isSuccessful
    }

    suspend fun isBookLiked(userId: Long, bookId: Long): Boolean {
        val resp = api.getLikedBook(userId, bookId)
        return resp.isSuccessful && resp.body() != null
    }

    suspend fun unlikeBook(userId: Long, bookId: Long): Boolean {
        return api.deleteLikedBook(userId, bookId).isSuccessful
    }
}
