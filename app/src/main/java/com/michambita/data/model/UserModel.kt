package com.michambita.data.model

import com.michambita.domain.model.User

data class UserModel(
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val idEmpresa: String? = null,
    val ctrlAdmin: Boolean = false
)

