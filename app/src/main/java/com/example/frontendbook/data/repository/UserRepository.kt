package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.UserApiService
import com.example.frontendbook.data.api.dto.UserResponse
import com.example.frontendbook.data.model.BookInteractionRequest

class UserRepository(private val api: UserApiService) {

    // ← your existing seven‐parameter method stays as is:

    suspend fun sendBookInteraction(
        userId: Long,
        bookId: Long,
        isLiked: Boolean,
        isRead: Boolean,
        isInReadList: Boolean,
        comment: String? = null,
        rating: Int = 0
    ): Boolean {
        val req = BookInteractionRequest(
            userId     = userId,
            bookId     = bookId,
            liked      = isLiked,
            read       = isRead,
            inReadList = isInReadList,
            comment    = comment,
            rating     = rating
        )
        return api.sendBookInteraction(userId, req).isSuccessful
    }

    // ★ NEW OVERLOAD ★
    suspend fun sendBookInteraction(request: BookInteractionRequest): Boolean {
        return api.sendBookInteraction(request.userId, request).isSuccessful
    }

    suspend fun fetchUser(userId: Long): UserResponse =
        api.getUserById(userId)
}
