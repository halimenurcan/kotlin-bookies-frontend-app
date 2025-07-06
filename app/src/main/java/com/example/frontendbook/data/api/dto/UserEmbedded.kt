package com.example.frontendbook.data.api.dto

import com.example.frontendbook.data.remote.dto.UserDto
import com.google.gson.annotations.SerializedName

data class UserEmbedded(
    @SerializedName("userResponseDTOList")
    val users: List<UserDto> = emptyList()
)