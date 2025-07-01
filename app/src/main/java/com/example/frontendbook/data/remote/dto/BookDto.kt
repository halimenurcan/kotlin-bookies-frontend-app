package com.example.frontendbook.data.remote.dto

data class BookDto(
    val id: String,
    val title: String,
    val author: String,
    val imageUrl: String? = null,
    val description: String? = null
)
