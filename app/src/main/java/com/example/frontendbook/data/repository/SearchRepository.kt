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
    suspend fun searchAll(
        keyword: String,
        genres: List<String>? = null,
        languages: List<String>? = null
    ): List<CombinedSearchResult> = coroutineScope {
        val usersDeferred = async {
            api.searchUsers(keyword)
                .body()?.embedded?.users.orEmpty()
                .map { CombinedSearchResult.UserResult(it.toDomain()) }
        }
        val booksDeferred = async {
            api.searchBooks(keyword, genres, languages)
                .body()?.embedded?.books.orEmpty()
                .map { CombinedSearchResult.BookResult(it.toDomain()) }
        }
        usersDeferred.await() + booksDeferred.await()
    }
    suspend fun getGenres(): List<String> {
        return api.getAllGenres().body().orEmpty()
    }

    suspend fun getLanguages(): List<String> {
        return api.getAllLanguages().body().orEmpty()
    }
}