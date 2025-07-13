package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.ListsApiService
import com.example.frontendbook.data.model.AddBookToListRequest
import com.example.frontendbook.data.model.CreateListRequest
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.data.remote.dto.ListWithBooksDto

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
        val request = AddBookToListRequest(
            bookId = bookId,
            listId = listId
        )
        return try {
            val response = api.addBookToList(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
    suspend fun getListWithBooks(listId: Long): ListDto {
        return api.getListWithBooks(listId)
    }


    suspend fun getAllLists(): List<ListDto> {
        return api.getAllLists()
    }

}
