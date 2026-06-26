package com.michambita.domain.model

data class User(
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val companyId: String? = null,
    val ctrlAdmin: Boolean = false
)
