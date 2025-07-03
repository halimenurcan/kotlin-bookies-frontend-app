package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.LikedCommentsApiService
import com.example.frontendbook.data.model.LikedCommentRequest

class LikedCommentsRepository(
    private val api: LikedCommentsApiService
) {
    suspend fun isLiked(userId: Long, commentId: Long): Boolean {
        val resp = api.isLiked(userId, commentId)
        return resp.isSuccessful && resp.body() == true
    }

    suspend fun getLikeCount(commentId: Long): Int {
        val resp = api.getLikeCount(commentId)
        return if (resp.isSuccessful) resp.body() ?: 0 else 0
    }

    suspend fun likeComment(userId: Long, commentId: Long): Boolean {
        val req = LikedCommentRequest(userId, commentId)
        val resp = api.likeComment(req)
        return resp.isSuccessful
    }

    suspend fun unlikeComment(userId: Long, commentId: Long): Boolean {
        val resp = api.unlikeComment(userId, commentId)
        return resp.isSuccessful
    }
}
