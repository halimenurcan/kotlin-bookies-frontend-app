package com.example.frontendbook.data.remote.dto

import com.example.frontendbook.domain.model.User
data class UserDto(
    val id: String?,
    val username: String?,
    val fullName: String?,
    val profileImageUrl: String?
)

fun UserDto.toDomain() = User(
    id = this.id ?: "",                // null gelirse boş string
    username = this.username ?: "",
    fullName = this.fullName ?: "",
    profileImageUrl = this.profileImageUrl
)
