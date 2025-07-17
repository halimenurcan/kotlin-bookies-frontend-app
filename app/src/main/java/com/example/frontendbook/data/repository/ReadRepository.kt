package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.dto.SimpleReadRequest
import com.example.frontendbook.data.api.service.ReadApiService
import com.example.frontendbook.data.model.ReadEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReadRepository(private val api: ReadApiService) {

    suspend fun isBookReadByUser(userId: Long, bookId: Long): Boolean {
        val readBooks = getReadBooks(userId)
        return readBooks.any { it.bookId.toString() == bookId.toString() }
    }
    suspend fun isBookReadListByUser(userId: Long, bookId: Long): Boolean {
        val readBooks = getToReadList(userId)
        return readBooks.any { it.bookId.toString() == bookId.toString() }
    }
    // --- Okunacak kitaplar (to-read list) için ---
    suspend fun getToReadList(userId: Long): List<ReadEntry> = withContext(Dispatchers.IO) {
        api.getReadListByUserId(userId).map {
            ReadEntry(
                id = 0L,
                userId = it.userId,
                bookId = it.bookId.toString(),
                bookTitle = "",
                bookAuthor = "",
                bookIsbn = "",
                bookDescription = "",
                bookPageCount = null,
                bookPublisher = "",
                bookPublishedYear = null,
                bookCoverUrl = "",

            )
        }
    }

    suspend fun addToReadList(userId: Long, bookId: Long): Boolean = withContext(Dispatchers.IO) {
        val req = SimpleReadRequest(userId, bookId)
        api.addToReadList(req).isSuccessful
    }

    suspend fun removeFromReadList(userId: Long, bookId: Long): Boolean = withContext(Dispatchers.IO) {
        api.deleteFromReadList(userId, bookId).isSuccessful
    }

    // --- Okunmuş kitaplar (read) için ---
    suspend fun getReadBooks(userId: Long): List<ReadEntry> = withContext(Dispatchers.IO) {
        api.getReadByUserId(userId)
    }

    suspend fun addToReadBooks(userId: Long, bookId: Long): Boolean = withContext(Dispatchers.IO) {
        val req = SimpleReadRequest(userId, bookId)
        api.addToRead(req).isSuccessful
    }

    suspend fun removeFromReadBooks(userId: Long, bookId: Long): Boolean = withContext(Dispatchers.IO) {
        api.deleteFromRead(userId, bookId).isSuccessful
    }
}
