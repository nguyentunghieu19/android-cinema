package com.cinema.android.domain.model

data class Seat(
    val id: Int,
    val row: String,
    val number: Int,
    val status: SeatStatus
)

enum class SeatStatus {
    AVAILABLE,
    BOOKED,
    UNKNOWN;

    companion object {
        fun fromString(value: String?): SeatStatus {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}