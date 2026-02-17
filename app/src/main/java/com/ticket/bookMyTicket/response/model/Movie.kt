package com.ticket.bookMyTicket.response.model

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val releaseDate: String,
    val rating: Double
)