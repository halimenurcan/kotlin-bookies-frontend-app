package com.example.frontendbook.domain.model.googleapi

import com.example.frontendbook.data.model.ImageLinks

data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val language: String?,
    val imageLinks: ImageLinks?,
    val categories: List<String>?,
    val averageRating: Double?
)
