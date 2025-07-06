package com.example.frontendbook.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long,
    val username: String,
    val fullName: String?,
    val profileImageUrl: String?
): Parcelable
