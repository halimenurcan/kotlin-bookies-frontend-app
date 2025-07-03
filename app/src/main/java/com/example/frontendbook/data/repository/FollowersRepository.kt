package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.FollowersApiService
import com.example.frontendbook.data.model.FollowerRequest
import com.example.frontendbook.data.model.FollowerResponse

class FollowersRepository(
    private val api: FollowersApiService
) {
    suspend fun follow(userId: Long, followerId: Long): Boolean =
        api.createFollower(FollowerRequest(userId, followerId)).isSuccessful

    suspend fun isFollowing(userId: Long, followerId: Long): Boolean {
        val resp = api.getFollower(userId, followerId)
        return resp.isSuccessful && resp.body() != null
    }

    suspend fun unfollow(userId: Long, followerId: Long): Boolean =
        api.deleteFollower(userId, followerId).isSuccessful
}
