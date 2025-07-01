package com.example.frontendbook.domain.mapper

import com.example.frontendbook.data.remote.dto.UserDto
import com.example.frontendbook.domain.model.User

object UserMapper {
    fun fromDto(dto: UserDto): User? {
        val id = dto.id ?: return null
        val username = dto.username ?: return null

        return User(
            id = id,
            username = username,
            fullName = dto.fullName,
            profileImageUrl = dto.profileImageUrl
        )
    }
}
