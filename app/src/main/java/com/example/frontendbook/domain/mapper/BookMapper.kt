package com.example.frontendbook.domain.mapper

import android.util.Log
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.domain.model.googleapi.BookItem

object BookMapper {
    fun fromApi(item: BookItem): Book? {
        val info = item.volumeInfo ?: return null

        val title = info.title ?: return null
        val author = info.authors?.firstOrNull() ?: "Unknown"
        val description = info.description ?: "No description"


        println("📘 MAPPING BOOK: $title")
        Log.d("BookMapper", "Thumbnail: ${info.imageLinks?.thumbnail}")

        return Book(
            id = item.id?.hashCode()?.toLong() ?: 0L, // → String ID'yi Long'a çevirmek için hash kullanıyoruz
            title = title,
            author = author,
            year = info.publishedDate?.take(4)?.toIntOrNull() ?: 0,
            genre = info.categories?.firstOrNull() ?: "Unknown",
            country = "Unknown",
            language = info.language ?: "en",
            popularity = 0,
            rating = info.averageRating?.toDouble() ?: 0.0,
            imageUrl = info.imageLinks?.thumbnail ?: info.imageLinks?.smallThumbnail,
            pageCount = info.pageCount ?: 0,
            description = description
        )

    }


}
