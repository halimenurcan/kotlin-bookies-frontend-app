package com.example.frontendbook.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val title: String,
    val author: String,
    val year: Int,
    val genre: String? = null,
    val country: String? = null,
    val language: String? = null,
    val popularity: Int = 0,
    val rating: Double = 0.0,
    val imageUrl: String? = null,
    val pageCount: Int =0,
    val description: String,
) :Parcelable
