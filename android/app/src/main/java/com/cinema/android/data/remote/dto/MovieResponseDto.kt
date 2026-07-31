package com.cinema.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("duration") val duration: Int?,
    @SerializedName("genre") val genre: String?,
    @SerializedName("director") val director: String?,
    @SerializedName("actors") val actors: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("rated") val rated: String?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("poster") val poster: String?,
    @SerializedName("trailer") val trailer: String?,
    @SerializedName("status") val status: Boolean?
)