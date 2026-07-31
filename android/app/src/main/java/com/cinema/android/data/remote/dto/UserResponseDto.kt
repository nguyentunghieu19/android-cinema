package com.cinema.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String?,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("fullname")
    val fullname: String?,

    @SerializedName("avatar")
    val avatar: String?,

    @SerializedName("status")
    val status: Boolean?,

    @SerializedName("role")
    val role: String?
)