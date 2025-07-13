package com.example.frontendbook.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val id: Long,
    val author: String?,
    val title: String,
    val isbn: String,
    val description: String?,
    val coverImageUrl: String?,
    val pageCount: Int?,
    val publisher: String?,
    val publishedYear: Int?,
    val rating: Int?
) : Parcelable
