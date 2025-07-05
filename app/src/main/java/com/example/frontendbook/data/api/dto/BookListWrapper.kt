package com.example.frontendbook.data.api.dto

import com.example.frontendbook.data.remote.dto.BookDto
import com.google.gson.annotations.SerializedName

data class BooksListWrapper(
    @SerializedName("bookResponseDTOList")
    val books: List<BookDto>
)