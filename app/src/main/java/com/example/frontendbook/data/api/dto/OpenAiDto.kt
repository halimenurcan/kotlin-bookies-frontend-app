package com.example.frontendbook.data.api.dto

class OpenAiDto {
    data class ThreadResponse(val id: String)
    data class RunRequest(val assistant_id: String)
    data class RunStatusResponse(val status: String)
    data class MessageResponse(
        val data: List<MessageData>
    )
    data class MessageRequest(
        val role: String = "user", // Bu önemli
        val content: String
    )
    data class ThreadCreateRequest(val dummy: String = "")

    data class MessageData(
        val id: String,
        val content: List<MessageContent>
    )

    data class MessageContent(
        val text: MessageText
    )

    data class MessageText(
        val value: String
    )
    data class RunResponse(
        val id: String,
        val objectType: String,  // örn: "thread.run"
        val createdAt: Long,
        val threadId: String,
        val assistantId: String,
        val status: String,      // örn: "queued", "in_progress", "completed", "failed"
        val startedAt: Long?,    // opsiyonel
        val expiresAt: Long?,    // opsiyonel
        val completedAt: Long?,  // opsiyonel
        val failedAt: Long?,     // opsiyonel
        val lastError: LastError?, // opsiyonel
        val model: String?,
        val instructions: String?,
        val tools: List<Any>?,
        val fileIds: List<String>?,
        val metadata: Map<String, String>?
    )

    data class LastError(
        val code: String?,
        val message: String?
    )
}