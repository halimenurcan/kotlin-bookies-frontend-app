package com.example.frontendbook.data.repository

import FollowersApiService
import com.example.frontendbook.data.api.dto.UserResponse
import com.example.frontendbook.data.model.FollowerRequest

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

    suspend fun getFollowersOfUser(userId: Long): List<UserResponse> {
        val resp = api.getFollowersOfUser(userId)
        if (!resp.isSuccessful) throw Exception("Followers could not be retrieved: ${resp.code()}")
        return resp.body()?._embedded?.userResponseDTOList ?: emptyList()
    }
    suspend fun getFollowingOfUser(userId: Long): List<UserResponse> {
        val resp = api.getFollowingOfUser(userId)
        if (!resp.isSuccessful) throw Exception("Following could not be retrieved: ${resp.code()}")
        return resp.body()?._embedded?.userResponseDTOList ?: emptyList()
    }


    suspend fun getFollowerCount(userId: Long): Int {
         return api.getFollowerCount(userId)

    }

    suspend fun getFollowingCount(userId: Long): Int {
        return api.getFollowingCount(userId)
    }
}
