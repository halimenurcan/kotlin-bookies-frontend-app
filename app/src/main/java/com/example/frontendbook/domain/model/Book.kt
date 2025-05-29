package com.example.frontendbook.domain.model

data class Book(
    val title: String,
    val author: String,
    val year: Int,
    val imageUrl: String? = null
)
