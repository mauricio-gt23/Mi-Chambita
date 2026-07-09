package com.michambita.core.common.util

object ValidateUtil {

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && EMAIL_REGEX.matches(email)
    }


}