package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.StatusApiService
import com.example.frontendbook.data.model.StatusBook

class StatusRepository(
    private val api: StatusApiService
) {
    suspend fun getReadBooks(userId: Long): List<StatusBook> {
        val resp = api.getReadBooks(userId)
        if (!resp.isSuccessful) throw Exception("The books that were read could not be obtained: ${resp.code()}")
        return resp.body() ?: emptyList()
    }

    suspend fun getWillReadBooks(userId: Long): List<StatusBook> {
        val resp = api.getWillReadBooks(userId)
        if (!resp.isSuccessful) throw Exception("Books to read could not be obtained: ${resp.code()}")
        return resp.body() ?: emptyList()
    }

    suspend fun addReadBook(request: StatusBook): StatusBook {
        val resp = api.addReadBook(request)
        if (!resp.isSuccessful) throw Exception("Addition failed: ${resp.code()}")
        return resp.body()!!
    }

    suspend fun addWillReadBook(request: StatusBook): StatusBook {
        val resp = api.addWillReadBook(request)
        if (!resp.isSuccessful) throw Exception("Addition failed: ${resp.code()}")
        return resp.body()!!
    }
}
