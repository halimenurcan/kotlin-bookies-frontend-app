package com.example.frontendbook.data.repository

import android.util.Log
import com.example.frontendbook.data.api.dto.SimpleReadRequest
import com.example.frontendbook.data.api.service.ReadApiService
import com.example.frontendbook.data.model.ReadEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReadRepository(private val api: ReadApiService) {

    suspend fun isBookReadByUser(userId: Long, bookId: Long): Boolean {
        val readBooks = getReadBooks(userId)
        return readBooks.any { it.bookId == bookId }
    }
    suspend fun isBookReadListByUser(userId: Long, bookId: Long): Boolean {
        val readBooks = getToReadList(userId)
        return readBooks.any { it.bookId.toString() == bookId.toString() }
    }
    suspend fun getToReadList(userId: Long): List<ReadEntry> = withContext(Dispatchers.IO) {
        val entries = api.getReadListByUserId(userId)

        entries.mapNotNull { entry ->
            try {
                val book = api.getBookById(entry.id)

                ReadEntry(
                    userId = userId,
                    bookId = book.id,
                    bookTitle = book.title,
                    bookAuthor = book.author.name,
                    bookIsbn = book.isbn,
                    bookDescription = book.description,
                    bookPageCount = book.pageCount,
                    bookPublisher = book.publisher,
                    bookPublishedYear = book.publishedYear,
                    bookCoverUrl = book.coverImageUrl.toString()
                )
            } catch (e: Exception) {
                null
            }
        }
    }


    suspend fun addToReadList(userId: Long, bookId: Long): Boolean = withContext(Dispatchers.IO) {
        val req = SimpleReadRequest(userId, bookId)
        api.addToReadList(req).isSuccessful
    }

    suspend fun removeFromReadList(userId: Long, bookId: Long): Boolean = withContext(Dispatchers.IO) {
        api.deleteFromReadList(userId, bookId).isSuccessful
    }

    suspend fun getReadBooks(userId: Long): List<ReadEntry> = withContext(Dispatchers.IO) {
        val entries = api.getReadByUserId(userId)

        Log.d("READ_DEBUG", " getReadByUserId response (${entries.size} adet):")
        entries.forEachIndexed { index, entry ->
            Log.d("READ_DEBUG", " [$index] Entry: userId=${userId}, bookId=${entry.id}")
        }

        entries.mapNotNull { entry ->
            val bookId = entry.id
            if (bookId == null || bookId == 0L) {
                Log.e("READ_DEBUG", " Invalid bookId: $bookId")
                return@mapNotNull null
            }

            try {
                val book = api.getBookById(bookId)
                Log.d("READ_DEBUG", "Books fetched: ${book.title} (${book.id})")

                ReadEntry(
                    userId = userId,
                    bookId = book.id,
                    bookTitle = book.title,
                    bookAuthor = book.author.name,
                    bookIsbn = book.isbn,
                    bookDescription = book.description,
                    bookPageCount = book.pageCount,
                    bookPublisher = book.publisher,
                    bookPublishedYear = book.publishedYear,
                    bookCoverUrl = book.coverImageUrl.toString()
                )
            } catch (e: Exception) {
                Log.e("READ_DEBUG", "Books could not be fetched: $e")
                null
            }
        }
    }



    suspend fun addToReadBooks(userId: Long, bookId: Long): Boolean = withContext(Dispatchers.IO) {
        val req = SimpleReadRequest(userId, bookId)
        api.addToRead(req).isSuccessful
    }

    suspend fun removeFromReadBooks(userId: Long, bookId: Long): Boolean = withContext(Dispatchers.IO) {
        api.deleteFromRead(userId, bookId).isSuccessful
    }
}
