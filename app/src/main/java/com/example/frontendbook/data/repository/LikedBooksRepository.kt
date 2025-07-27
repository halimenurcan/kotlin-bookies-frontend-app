package com.example.frontendbook.data.repository

import android.util.Log
import com.example.frontendbook.data.api.mapper.BookMapper
import com.example.frontendbook.data.api.service.LikedBooksApiService
import com.example.frontendbook.data.model.LikedBookRequest
import com.example.frontendbook.domain.model.Book
import kotlin.collections.map
import kotlin.collections.orEmpty

class LikedBooksRepository(
    private val api: LikedBooksApiService
) {

    suspend fun likeBook(userId: Long, bookId: Long): Boolean {
        val req = LikedBookRequest(userId = userId, bookId = bookId)
        return api.createLikedBook(req).isSuccessful
    }
    suspend fun getAllLikedBooks(userId : Long) : List<Book> {
        val resp = api.getAllLikedBooks(userId)
        return if (resp.isSuccessful) {
            resp.body()?.map { BookMapper.fromDto(it) }.orEmpty()
        } else {
            val err = resp.errorBody()?.string()
            Log.e("BookRepo", "fetchAllLikedBooks failed: code=${resp.code()}, body=$err")
            throw Exception("Favorite books could not be loaded: ${resp.code()}")
        }
    }
    suspend fun isBookLiked(userId: Long, bookId: Long): Boolean {
        val resp = api.getLikedBook(userId, bookId)
        return resp.isSuccessful && resp.body() != null
    }

    suspend fun unlikeBook(userId: Long, bookId: Long): Boolean {
        return api.deleteLikedBook(userId, bookId).isSuccessful
    }
}
