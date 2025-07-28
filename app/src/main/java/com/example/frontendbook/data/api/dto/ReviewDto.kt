package com.example.frontendbook.data.api.dto

import android.os.Parcelable
import com.example.frontendbook.data.remote.dto.UserDto
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ReviewDto(
    val id: Long,
    val userId: Long?,
    val bookId: Long,
    val bookCoverUrl: String,
    val userName: String,
    val score: Int?,
    @SerializedName("content")
    val comment: String,
    val createdAt: String,
    var isLiked: Boolean,

    val book:@RawValue BookDto? = null,
    val user:@RawValue UserDto? =null

) : Parcelable
