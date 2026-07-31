package com.cinema.android.domain.model

enum class UserRole {
    USER,
    STAFF,
    ADMIN;

    companion object {
        fun fromString(value: String?): UserRole {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: USER
        }
    }
}