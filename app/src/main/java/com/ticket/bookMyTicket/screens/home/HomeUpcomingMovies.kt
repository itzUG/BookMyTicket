package com.ticket.bookMyTicket.screens.home

import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDto

data class HomeUpcomingMovies(
    val isLoading: Boolean = false,
    val upcomingMovies: List<MovieDto> = emptyList(),
    val error: String? = null
)


data class HomeTopMoviesState(
    val isLoading: Boolean,
    val topMovies : List<MovieDto> = emptyList(),
    val error: String? = null
)


data class HomePopularMovies(
    val isLoading: Boolean,
    val topMovies : List<MovieDto> = emptyList(),
    val error: String? = null
)



data class HomeNowPlayingMovies(
    val isLoading: Boolean,
    val topMovies : List<MovieDto> = emptyList(),
    val error: String? = null
)