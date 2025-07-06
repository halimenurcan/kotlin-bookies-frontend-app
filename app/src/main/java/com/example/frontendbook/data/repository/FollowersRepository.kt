package com.example.frontendbook.data.repository

import FollowersApiService
import com.example.frontendbook.data.model.FollowerRequest
import com.example.frontendbook.data.model.FollowerResponse

class FollowersRepository(
    private val api: FollowersApiService
) {

    suspend fun follow(followerId: Long, followedId: Long): Boolean =
        api.createFollower(FollowerRequest(followerId, followedId)).isSuccessful

    suspend fun isFollowing(userId: Long, followerId: Long): Boolean {
        val resp = api.getFollower(userId, followerId)
        return resp.isSuccessful && resp.body() != null
    }

    suspend fun unfollow(userId: Long, followerId: Long): Boolean {
        return api.deleteFollower(userId, followerId).isSuccessful
    }

    suspend fun getFollowersOfUser(userId: Long): List<FollowerResponse> {
        val resp = api.getFollowersOfUser(userId)
        if (!resp.isSuccessful) throw Exception("Takipçiler alınamadı: ${resp.code()}")
        return resp.body() ?: emptyList()
    }

    suspend fun getFollowingOfUser(userId: Long): List<FollowerResponse> {
        val resp = api.getFollowingOfUser(userId)
        if (!resp.isSuccessful) throw Exception("Takip edilenler alınamadı: ${resp.code()}")
        return resp.body() ?: emptyList()
        }
}