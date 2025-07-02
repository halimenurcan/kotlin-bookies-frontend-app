package com.example.frontendbook.data.api

import com.example.frontendbook.data.remote.dto.CombinedSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("volumes")
    suspend fun searchEverything(@Query("q") query: String): CombinedSearchResponse
}
