package com.example.frontendbook.data.api.service

import com.example.frontendbook.data.api.dto.UserResponse
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

    @POST("users/{userId}/books/interact")
    suspend fun sendBookInteraction(
        @Path("userId") userId: Long,
        @Body interaction: BookInteractionRequest
    ): Response<Unit>

    @GET("users/search")
    suspend fun searchUsers(@Query("username") username: String): List<UserDto>
    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") userId: Long): UserResponse

    @GET("api/users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<UserResponse>

    @PUT("api/users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Long,
        @Body updated: UserResponse
    ): UserResponse

    @DELETE("api/users/{id}")
    suspend fun deleteUser(@Path("id") userId: Long)
    @GET("api/users/{userId}/followers")
    suspend fun getFollowers(
        @Path("userId") userId: Long
    ): Response<List<UserDto>>

    /** Takip ettiklerim listesi */
    @GET("api/users/{userId}/following")
    suspend fun getFollowing(
        @Path("userId") userId: Long
    ): Response<List<UserDto>>

    /** Bir kullanıcıyı takip et */
    @POST("api/users/{targetId}/follow")
    suspend fun followUser(
        @Path("targetId") targetUserId: Long
    ): Response<Unit>

    /** Bir kullanıcıyı takipten bırak */
    @DELETE("api/users/{targetId}/follow")
    suspend fun unfollowUser(
        @Path("targetId") targetUserId: Long
    ): Response<Unit>

    /** İki kullanıcı arasındaki takip durumunu kontrol et */
    @GET("api/users/{currentId}/following/{targetId}")
    suspend fun isFollowing(
        @Path("currentId") currentUserId: Long,
        @Path("targetId") targetUserId: Long
    ): Response<Boolean>
}