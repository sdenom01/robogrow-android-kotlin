package io.robogrow.classes

data class AuthenticatedUser(
    val user: User,
    val token: String
)