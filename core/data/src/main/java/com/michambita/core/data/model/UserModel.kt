package com.michambita.core.data.model

import com.michambita.core.domain.model.User

data class UserModel(
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val idEmpresa: String? = null,
    val ctrlAdmin: Boolean = false
)

fun UserModel.toDomain() = User(userId, name, email, null, null, idEmpresa, ctrlAdmin)
