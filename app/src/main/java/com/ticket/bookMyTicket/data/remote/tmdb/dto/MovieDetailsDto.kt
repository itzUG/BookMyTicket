package com.ticket.bookMyTicket.data.remote.tmdb.dto

data class MovieDetailsDto(
    val id: Int? = null,
    val title: String? = null,
    val overview: String? = null,
    val runtime: Int? = null,
    val release_date: String? = null,
    val vote_average: Double? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val genres: List<GenreDto>? = null,
    val credits: CreditsResponseDto? = null,
    val videos: VideoResponseDto? = null,
    val reviews: ReviewResponseDto? = null
)
