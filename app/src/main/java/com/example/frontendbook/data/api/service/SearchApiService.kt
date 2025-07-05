package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.remote.dto.CombinedSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("books/search/")
    suspend fun searchEverything(@Query("q") query: String): CombinedSearchResponse
}