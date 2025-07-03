package com.example.frontendbook.data.api
import com.example.frontendbook.data.model.BookInteractionRequest
import com.example.frontendbook.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.frontendbook.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {

    @POST("users/{userId}/books/interact")
    suspend fun sendBookInteraction(
        @Path("userId") userId: Long,
        @Body interaction: BookInteractionRequest
    ): Response<Unit>

    @GET("users/search")
    suspend fun searchUsers(@Query("username") username: String): List<UserDto>

}
