package com.ticket.bookMyTicket.data.remote.tmdb.dto

data class ReviewsResponseDto(
    val page: Int,
    val results: List<ReviewDto>
)
