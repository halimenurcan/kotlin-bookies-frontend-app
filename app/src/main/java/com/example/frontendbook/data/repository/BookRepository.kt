package com.example.frontendbook.data.repository

import android.content.Context
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.domain.mapper.BookMapper
import com.example.frontendbook.domain.mapper.UserMapper
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.domain.model.User

class BookRepository(private val context: Context) {

    suspend fun searchBooks(query: String): List<Book> {
        return try {
            val response = RetrofitClient.booksApiService.searchBooks(query)
            val items = response.items ?: emptyList()
            items.mapNotNull { BookMapper.fromApi(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchUsers(username: String): List<User> {
        return try {
            val response = RetrofitClient.userApiService(context).searchUsers(username)
            response.mapNotNull { UserMapper.fromDto(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchEverything(query: String): Pair<List<Book>, List<User>> {
        val books = searchBooks(query)
        val users = searchUsers(query)
        return Pair(books, users)
    }


}
