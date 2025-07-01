package com.example.frontendbook.data.repository

import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.domain.mapper.BookMapper
import com.example.frontendbook.domain.mapper.UserMapper
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.domain.model.User

class BookRepository {

    // Kitap araması (örneğin: Google Books API)
    suspend fun searchBooks(query: String): List<Book> {
        val response = RetrofitClient.booksApiService.searchBooks(query)
        val items = response.items ?: emptyList()
        println("📚 Raw Book Items: ${items.mapNotNull { it.volumeInfo?.title }}")
        return items.mapNotNull { BookMapper.fromApi(it) }
    }

    // Kullanıcı araması (örneğin: /users/search?username=...)
    suspend fun searchUsers(username: String): List<User> {
        val response = RetrofitClient.userApiService.searchUsers(username)
        println("👤 Raw User Items: ${response.map { it.username }}")
        return response.mapNotNull { UserMapper.fromDto(it) }
    }

    // Kombine arama: kitap ve kullanıcı birlikte döner
    suspend fun searchEverything(query: String): Pair<List<Book>, List<User>> {
        val books = searchBooks(query)
        val users = searchUsers(query)
        return Pair(books, users)
    }
}
