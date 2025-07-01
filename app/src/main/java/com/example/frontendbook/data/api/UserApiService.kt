package com.example.frontendbook.data.api
import com.example.frontendbook.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("users/search")
    suspend fun searchUsers(@Query("username") username: String): List<UserDto>
}
