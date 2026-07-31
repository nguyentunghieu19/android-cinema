// ShowtimeResponseDto.kt
package com.cinema.android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ShowtimeResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("movieId") val movieId: Int,
    @SerializedName("movieTitle") val movieTitle: String,
    @SerializedName("roomId") val roomId: Int,
    @SerializedName("roomName") val roomName: String,
    @SerializedName("cinemaId") val cinemaId: Int,
    @SerializedName("cinemaName") val cinemaName: String,
    @SerializedName("startTime") val startTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("price") val price: Double,
    @SerializedName("status") val status: Boolean?
)