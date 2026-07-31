package com.cinema.android.domain.model

import java.time.LocalDate

data class Movie(
    val id: Int,
    val title: String,
    val description: String?,
    val duration: Int?,
    val genre: String?,
    val director: String?,
    val actors: String?,
    val language: String?,
    val rated: String?,
    val releaseDate: LocalDate?,
    val posterUrl: String?,
    val trailer: String?,
    val isActive: Boolean
)