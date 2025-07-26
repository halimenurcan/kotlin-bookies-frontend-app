package com.example.frontendbook.data.api.dto


import com.google.gson.annotations.SerializedName

data class EmbeddedUsersResponse(
    @SerializedName("_embedded")
    val embedded: UserEmbedded?
) {

}