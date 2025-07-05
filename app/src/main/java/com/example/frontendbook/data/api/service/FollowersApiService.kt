package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.model.FollowerRequest
import com.example.frontendbook.data.model.FollowerResponse
import retrofit2.Response
import retrofit2.http.*

interface FollowersApiService {

    /**
     * Create a new follower relation
     * POST /api/followers
     */
    @POST("followers")
    suspend fun createFollower(
        @Body request: FollowerRequest
    ): Response<Unit>

    /**
     * Get a follower relation by composite ID
     * GET /api/followers?userId={userId}&followerId={followerId}
     */
    @GET("followers")
    suspend fun getFollower(
        @Query("userId") userId: Long,
        @Query("followerId") followerId: Long
    ): Response<FollowerResponse>

    /**
     * Delete a follower relation by composite ID
     * DELETE /api/followers?userId={userId}&followerId={followerId}
     */
    @DELETE("followers")
    suspend fun deleteFollower(
        @Query("userId") userId: Long,
        @Query("followerId") followerId: Long
    ): Response<Unit>
}
