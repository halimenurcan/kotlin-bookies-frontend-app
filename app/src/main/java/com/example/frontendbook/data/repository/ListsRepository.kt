package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.ListsApiService
import com.example.frontendbook.data.model.AddBookRequest
import com.example.frontendbook.data.model.CreateListRequest
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

    suspend fun createList(userId: Long, title: String): Boolean {
        val body = CreateListRequest(userId = userId, name = title)
        val resp = api.createList(body)
        return resp.isSuccessful
    }

    suspend fun addBookToList(listId: Long, bookId: Long): Boolean {
        val request = AddBookRequest(
            bookId = bookId
        )
        return try {
            val response = api.addBookToList(listId, request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
