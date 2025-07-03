package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.UserApiService
import com.example.frontendbook.data.remote.dto.UserDto

class UserFollowRepository(
    private val api: UserApiService
) {
    suspend fun getFollowers(userId: Long): List<UserDto> {
        val resp = api.getFollowers(userId)
        if (resp.isSuccessful) return resp.body().orEmpty()
        throw Exception("Takipçiler alınamadı: ${resp.code()}")
    }

    suspend fun getFollowing(userId: Long): List<UserDto> {
        val resp = api.getFollowing(userId)
        if (resp.isSuccessful) return resp.body().orEmpty()
        throw Exception("Takip ettiklerim alınamadı: ${resp.code()}")
    }

    suspend fun followUser(targetUserId: Long): Boolean {
        val resp = api.followUser(targetUserId)
        return resp.isSuccessful
    }

    suspend fun unfollowUser(targetUserId: Long): Boolean {
        val resp = api.unfollowUser(targetUserId)
        return resp.isSuccessful
    }

    suspend fun isFollowing(currentUserId: Long, targetUserId: Long): Boolean {
        val resp = api.isFollowing(currentUserId, targetUserId)
        return resp.isSuccessful && resp.body() == true
    }
}
