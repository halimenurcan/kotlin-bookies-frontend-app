package com.example.frontendbook.data.api.dto

import com.google.gson.annotations.SerializedName

data class AuthorDto(
    @SerializedName("id")   val id: Long?,
    @SerializedName("name") val name: String
)