package com.example.frontendbook.data.api.dto


data class ListDto(
    val id: Long,
    val title: String,
    val description: String?,
    val books: List<BookDto> ,
    val owner: OwnerDto
)

data class OwnerDto(
    val id: Long,
    val username: String
)

