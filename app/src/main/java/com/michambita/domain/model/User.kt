package com.michambita.domain.model

import com.michambita.data.model.UserModel

data class User (
    val userId: String?,
    val name: String?,
    val email: String?
)

fun UserModel.toDomain() = User(userId, name, email)