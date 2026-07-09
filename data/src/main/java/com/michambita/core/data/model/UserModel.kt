package com.michambita.data.model

import com.michambita.domain.model.User

data class UserModel(
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val companyId: String? = null,
    val ctrlAdmin: Boolean = false
)

fun UserModel.toDomain() = User(userId, name, email, null, null, companyId, ctrlAdmin)
