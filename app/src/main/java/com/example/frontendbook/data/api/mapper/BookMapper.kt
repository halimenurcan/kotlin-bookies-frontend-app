package com.example.frontendbook.data.api.mapper
import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.domain.model.Book
object BookMapper {
    fun fromDto(dto: BookDto): Book =
        Book(
            id = dto.id,
            author = dto.author.name.ifBlank { "Unknown" },
            title = dto.title.ifBlank { "Untitled" },
            isbn = dto.isbn,
            description = dto.description.ifBlank { "No description available" },
            coverImageUrl = dto.coverImageUrl,
            pageCount = dto.pageCount ?: 0,
            publisher = dto.publisher.ifBlank { "Unknown publisher" },
            publishedYear = dto.publishedYear ?: 0,
            rating = dto.rating
        )
}