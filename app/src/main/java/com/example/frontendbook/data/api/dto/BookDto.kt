package com.example.frontendbook.data.api.dto

import com.example.frontendbook.domain.model.Book
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
fun BookDto.toDomain() = Book(
    id = this.id,
    title = this.title,
    author = this.author.name,
    coverImageUrl = this.coverImageUrl,
    isbn = this.isbn,
    description =this.description,
    pageCount = this.pageCount,
    publisher = this.publisher,
    publishedYear = this.publishedYear,
    rating = this.rating
)