package com.ticket.bookMyTicket.data.remote.tmdb.dto

data class ReviewResponseDto(
    val page: Int,
    val results: List<ReviewDto>,
    val total_pages: Int,
    val total_results: Int
)


data class ReviewDto(
    val author: String,
    val author_details: AuthorDetailsDto,
    val content: String,
    val created_at: String,
    val id: String,
    val updated_at: String,
    val url: String
)


data class AuthorDetailsDto(
    val name: String,
    val username: String,
    val avatar_path: String?,
    val rating: Double?
)
