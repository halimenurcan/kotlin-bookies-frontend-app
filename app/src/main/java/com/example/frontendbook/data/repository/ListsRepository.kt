package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.ListsApiService
import com.example.frontendbook.data.model.ListDto

class ListsRepository(
    private val api: ListsApiService
) {
    suspend fun getUserLists(userId: Long): List<ListDto> {
        val resp = api.getUserLists(userId)
        if (resp.isSuccessful) {
            return resp.body().orEmpty()
        }
        throw Exception("Listeler yüklenemedi: ${resp.code()}")
    }
}
