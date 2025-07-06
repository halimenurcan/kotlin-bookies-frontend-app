package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.SearchApiService
import com.example.frontendbook.data.api.dto.toDomain
import com.example.frontendbook.data.remote.dto.toDomain
import com.example.frontendbook.domain.model.CombinedSearchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SearchRepository(
    private val api: SearchApiService
) {
    suspend fun searchAll(keyword: String): List<CombinedSearchResult> = coroutineScope {
        val usersDeferred = async {
            api.searchUsers(keyword)
                .body()?.embedded?.users.orEmpty()
                .map { CombinedSearchResult.UserResult(it.toDomain()) }
        }
        val booksDeferred = async {
            api.searchBooks(keyword)
                .body()?.embedded?.books.orEmpty()
                .map { CombinedSearchResult.BookResult(it.toDomain()) }
        }
        usersDeferred.await() + booksDeferred.await()
    }
}