package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.AvatarResponse
import com.example.frontendbook.data.api.dto.UserResponse
import com.example.frontendbook.data.model.AvatarRequest
import com.example.frontendbook.data.model.BookInteractionRequest
import com.example.frontendbook.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("user-avatars/{userId}")
    suspend fun getAvatarByUserId(
        @Path("userId") userId: Long
    ): Response<AvatarResponse>

    @PUT("user-avatars/{userId}")
    suspend fun updateAvatar(
        @Path("userId") userId: Long,
        @Body avatarRequest: AvatarRequest
    ): Response<Unit>

    @POST("users/{userId}/books/interact")
    suspend fun sendBookInteraction(
        @Path("userId") userId: Long,
        @Body interaction: BookInteractionRequest
    ): Response<Unit>


    @POST("user-avatars")
    suspend fun createUserAvatar(
        @Body avatarRequest: AvatarRequest
    ): Response<Unit>

    @GET("users/search")
    suspend fun searchUsers(@Query("username") username: String): List<UserDto>
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Long): UserResponse

    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<UserResponse>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Long,
        @Body updated: UserResponse
    ): UserResponse

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: Long)
    @GET("followers/{userId}")
    suspend fun getFollowers(
        @Path("userId") userId: Long
    ): Response<List<UserDto>>


    @GET("followers/following/{userId}")
    suspend fun getFollowing(
        @Path("userId") userId: Long
    ): Response<List<UserDto>>


    @POST("users/{targetId}/follow")
    suspend fun followUser(
        @Path("targetId") targetUserId: Long
    ): Response<Unit>


    @DELETE("users/{targetId}/follow")
    suspend fun unfollowUser(
        @Path("targetId") targetUserId: Long
    ): Response<Unit>


    @GET("users/{currentId}/following/{targetId}")
    suspend fun isFollowing(
        @Path("currentId") currentUserId: Long,
        @Path("targetId") targetUserId: Long
    ): Response<Boolean>
}