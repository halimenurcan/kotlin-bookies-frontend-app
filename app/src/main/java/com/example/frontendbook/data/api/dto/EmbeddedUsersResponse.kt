package com.example.frontendbook.data.api.dto

import com.example.frontendbook.data.remote.dto.UserDto
import com.google.gson.annotations.SerializedName

data class EmbeddedUsersResponse(
    @SerializedName("_embedded")
    val embedded: UserEmbedded?
) {

}