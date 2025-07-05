// app/src/main/java/com/example/frontendbook/data/remote/dto/BookDto.kt
package com.example.frontendbook.data.remote.dto

import com.example.frontendbook.data.api.dto.AuthorDto
import com.google.gson.annotations.SerializedName

data class BookDto(
    @SerializedName("id")             val id: Long,
    @SerializedName("title")          val title: String,
    @SerializedName("isbn")           val isbn: String,
    @SerializedName("description")    val description: String,
    @SerializedName("coverImageUrl")  val coverImageUrl: String?,
    @SerializedName("pageCount")      val pageCount: Int?,
    @SerializedName("publisher")      val publisher: String,
    @SerializedName("publishedYear")  val publishedYear: Int?,
    @SerializedName("author")         val author: AuthorDto,
    @SerializedName("rating")         val rating: Int
)
