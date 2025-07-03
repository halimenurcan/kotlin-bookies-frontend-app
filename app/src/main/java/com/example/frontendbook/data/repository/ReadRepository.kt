package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.ReadApiService
import com.example.frontendbook.data.model.ReadEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReadRepository(private val api: ReadApiService) {

    // Kullanıcının read list'ini al
    suspend fun getReadList(userId: Long): List<ReadEntry> = withContext(Dispatchers.IO) {
        api.getReadListByUserId(userId)
    }

    // Kitabı read list'e ekle
    suspend fun addToReadList(entry: ReadEntry): ReadEntry = withContext(Dispatchers.IO) {
        api.addToReadList(entry)
    }

    // Kitabı read list'ten sil
    suspend fun removeFromReadList(entryId: Long) = withContext(Dispatchers.IO) {
        api.deleteFromReadList(entryId)
    }
}
