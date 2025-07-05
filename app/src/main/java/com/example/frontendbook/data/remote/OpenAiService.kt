package com.example.frontendbook.data.remote

import com.example.frontendbook.data.remote.dto.*
import retrofit2.http.*

interface OpenAiService {
    @POST("threads")
    suspend fun createThread(
        @Header("Authorization") token: String,
        @Header("OpenAI-Beta") beta: String = "assistants=v1"
    ): ThreadResponse

    @POST("threads/{thread_id}/messages")
    suspend fun sendMessageToThread(
        @Path("thread_id") threadId: String,
        @Header("Authorization") token: String,
        @Header("OpenAI-Beta") beta: String = "assistants=v1",
        @Body body: MessageRequest
    )

    @POST("threads/{thread_id}/runs")
    suspend fun runAssistant(
        @Path("thread_id") threadId: String,
        @Header("Authorization") token: String,
        @Header("OpenAI-Beta") beta: String = "assistants=v1",
        @Body body: RunRequest
    ): RunResponse

    @GET("threads/{thread_id}/messages")
    suspend fun getMessages(
        @Path("thread_id") threadId: String,
        @Header("Authorization") token: String,
        @Header("OpenAI-Beta") beta: String = "assistants=v1"
    ): MessageResponse
}
