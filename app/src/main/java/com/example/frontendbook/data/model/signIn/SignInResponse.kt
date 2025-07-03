package com.example.frontendbook.data.model.signIn

data class SignInResponse(
    val id: Long,
    val createdAt: String?,
    val updatedAt: String?,
    val username: String,
    val token: String,
    val _links: Links?
)

data class Links(
    val self: Link,
    val logout: Link,
    val profile: Link
)

data class Link(
    val href: String
)
