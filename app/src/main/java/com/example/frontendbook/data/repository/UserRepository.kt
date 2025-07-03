package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.UserApiService
import com.example.frontendbook.data.model.BookInteractionRequest

class UserRepository(private val api: UserApiService) {

    suspend fun sendBookInteraction(userId: Long, interaction: BookInteractionRequest): Boolean {
        val response = api.sendBookInteraction(userId, interaction)
        return response.isSuccessful
    }
}
