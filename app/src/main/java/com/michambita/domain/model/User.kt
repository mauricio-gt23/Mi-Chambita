package com.michambita.domain.model

import com.michambita.data.model.UserModel

data class User (
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val idEmpresa: String? = null,
    val ctrlAdmin: Boolean = false
)

fun UserModel.toDomain() = User(userId, name, email, null, null, idEmpresa, ctrlAdmin)