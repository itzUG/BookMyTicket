package com.ticket.bookMyTicket.response.model

data class Show(
    val id: String,
    val movieId: Int,
    val cinemaId: String,
    val time: String,
    val language: String
)
