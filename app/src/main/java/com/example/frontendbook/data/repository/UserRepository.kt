package com.example.frontendbook.data.repository

import android.util.Log
import com.example.frontendbook.data.api.dto.AvatarResponse
import com.example.frontendbook.data.api.service.UserApiService
import com.example.frontendbook.data.api.dto.UserResponse
import com.example.frontendbook.data.model.AvatarRequest
import com.example.frontendbook.data.model.BookInteractionRequest

class UserRepository(private val api: UserApiService) {
    private val TAG = "UserRepository"

    suspend fun getUsernameById(userId: Long): String {
        return try {
            api.getUserById(userId).username
        } catch (e: Exception) {
            Log.e("UserRepo", "Username fetch error", e)
            "unknown"
        }
    }




    suspend fun updateAvatar(userId: Long, avatar: Long): Boolean {
        return try {
            val response = api.updateAvatar(
                userId,
                AvatarRequest(userId,avatar)
            )
            if (!response.isSuccessful) {
                Log.e("UserRepository", "updateAvatar() -> Unsuccessful! HTTP ${response.code()} - Body: ${response.errorBody()?.string()}")
            }
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("UserRepository", "updateAvatar() -> Error: ${e.message}", e)
            false
        }
    }

    suspend fun getAvatarByUserId(userId: Long): AvatarResponse? {
        return try {
            val response = api.getAvatarByUserId(userId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(TAG, "getAvatarByUserId() -> Unsuccessful! HTTP ${response.code()} - Body: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getAvatarByUserId() -> Error: ${e.message}", e)
            null
        }
    }


    suspend fun fetchAvatar(userId: Long): AvatarResponse? {
        val response = api.getAvatarByUserId(userId)
        return if (response.isSuccessful) response.body() else null
    }


    suspend fun fetchUser(userId: Long): UserResponse {
        Log.d(TAG, "fetchUser() -> fetch, userId=$userId")
        val resp = api.getUserById(userId)
        Log.d(TAG, "fetchUser() -> user fetch: $resp")
        return resp
    }

    suspend fun getFollowersCount(userId: Long): Int {
        Log.d(TAG, "getFollowersCount() -> fetch")
        val resp = api.getFollowers(userId)
        val count = if (resp.isSuccessful) resp.body()?.size ?: 0 else 0
        Log.d(TAG, "getFollowersCount() -> count=$count (HTTP ${resp.code()})")
        return count
    }

    suspend fun getFollowingCount(userId: Long): Int {
        Log.d(TAG, "getFollowingCount() -> fetch")
        val resp = api.getFollowing(userId)
        val count = if (resp.isSuccessful) resp.body()?.size ?: 0 else 0
        Log.d(TAG, "getFollowingCount() -> count=$count (HTTP ${resp.code()})")
        return count
    }

    suspend fun sendBookInteraction(userId: Long, request: BookInteractionRequest): Boolean {
        return try {
            val response = api.sendBookInteraction(userId, request)
            Log.d(TAG, "sendBookInteraction() -> HTTP ${response.code()}")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "sendBookInteraction() -> Error: ${e.message}", e)
            false
        }
    }

}
