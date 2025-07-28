package com.example.frontendbook.data.remote

import com.example.frontendbook.data.api.dto.OpenAiDto
import com.example.frontendbook.data.api.dto.OpenAiDto.MessageResponse
import com.example.frontendbook.data.api.dto.OpenAiDto.RunResponse
import com.example.frontendbook.data.api.dto.OpenAiDto.ThreadCreateRequest
import com.example.frontendbook.data.api.dto.OpenAiDto.ThreadResponse
import retrofit2.http.*

interface OpenAiService {
    @POST("threads")
    suspend fun createThread(@Body request: ThreadCreateRequest): ThreadResponse

    @POST("threads/{thread_id}/messages")
    suspend fun sendMessageToThread(
        @Path("thread_id") threadId: String,
        @Body body: OpenAiDto.MessageRequest
    ): MessageResponse

    @POST("threads/{thread_id}/runs")
    suspend fun runAssistant(
        @Path("thread_id") threadId: String,
        @Body body: OpenAiDto.RunRequest
    ): RunResponse

    @GET("threads/{thread_id}/messages")
    suspend fun getMessages(
        @Path("thread_id") threadId: String
    ): OpenAiDto.MessageResponse
}
