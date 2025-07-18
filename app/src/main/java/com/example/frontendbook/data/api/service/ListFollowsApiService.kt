package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.model.ListFollowRequest
import com.example.frontendbook.data.model.ListFollowResponse
import retrofit2.Response
import retrofit2.http.*

interface ListFollowsApiService {

    @POST("list-follows")
    suspend fun followList(
        @Body request: ListFollowRequest
    ): Response<Unit>

    @POST("list-follows/unfollow")
    suspend fun unfollowList(
        @Body request: ListFollowRequest
    ): Response<Unit>

    @GET("list-follows/user/{userId}/list/{listId}")
    suspend fun getFollowRelation(
        @Path("userId") userId: Long,
        @Path("listId") listId: Long
    ): Response<ListFollowResponse>
    @GET("list-follows/user/{userId}")
    suspend fun getFollowedLists(
        @Path("userId") userId: Long
    ): Response<List<ListFollowResponse>>


    @GET("list-follows/user/{userId}/list/{listId}/is-following")
    suspend fun isFollowing(
        @Path("userId") userId: Long,
        @Path("listId") listId: Long
    ): Response<Boolean>

    @GET("list-follows/list/{listId}/count")
    suspend fun getFollowerCount(
        @Path("listId") listId: Long
    ): Response<Int>

    @DELETE("list-follows/user/{userId}/list/{listId}")
    suspend fun deleteFollow(
        @Path("userId") userId: Long,
        @Path("listId") listId: Long
    ): Response<Unit>
}
