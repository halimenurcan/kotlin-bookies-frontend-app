package com.example.frontendbook.data.repository

import com.example.frontendbook.data.api.service.ListFollowsApiService
import com.example.frontendbook.data.model.ListFollowRequest

class ListFollowsRepository(
    private val api: ListFollowsApiService
) {
    suspend fun followList(userId: Long, listId: Long): Boolean =
        api.followList(ListFollowRequest(userId, listId)).isSuccessful

    suspend fun unfollowList(userId: Long, listId: Long): Boolean =
        api.unfollowList(ListFollowRequest(userId, listId)).isSuccessful

    suspend fun isFollowing(userId: Long, listId: Long): Boolean {
        val resp = api.isFollowing(userId, listId)
        return resp.isSuccessful && resp.body() == true
    }

    suspend fun getFollowerCount(listId: Long): Int {
        val resp = api.getFollowerCount(listId)
        return if (resp.isSuccessful) resp.body() ?: 0 else 0
    }
    suspend fun getFollowedListIdsByUser(userId: Long): List<Long> {
        val resp = api.getFollowedLists(userId)
        return if (resp.isSuccessful) {
            resp.body()?.map { it.id } ?: emptyList()
        } else emptyList()
    }

}
