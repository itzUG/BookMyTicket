package com.ticket.bookMyTicket.response.model

data class Review(
    val id: String,
    val author: String,
    val content: String,
    val rating: Double?
)


