package com.cinema.android.domain.model

enum class BookingStatus {
    PENDING,
    PAID,
    CANCELLED,
    UNKNOWN;

    companion object {
        fun fromString(value: String?): BookingStatus {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}