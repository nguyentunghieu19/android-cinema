package com.cinema.android.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String?,
    val phone: String?,
    val fullname: String?,
    val avatar: String?,
    val isActive: Boolean,
    val role: UserRole
)