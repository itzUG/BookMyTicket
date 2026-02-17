package com.ticket.bookMyTicket.response.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val backdropUrl: String,
    val runtime: Int,
    val genres: List<String>,
    val rating: Double,
    val language: String,
    val cast: List<Cast>,
    val reviews: List<Review>,
    val trailerKey: String?
)
