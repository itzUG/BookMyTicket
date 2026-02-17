package com.ticket.bookMyTicket.data.repository

import com.ticket.bookMyTicket.data.remote.tmdb.TmdbClient
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDto
import com.ticket.bookMyTicket.utils.Constants

class MovieRepository {

    private val api = TmdbClient.tmdbApi

    suspend fun getNowPlaying(): List<MovieDto> {
        return api
            .getNowPlaying(Constants.API_KEY)
            .results
    }

    suspend fun getPopular(): List<MovieDto> {
        return api
            .getPopularMovies(Constants.API_KEY)
            .results
    }

    suspend fun getTopRated(): List<MovieDto> {
        return api
            .getTopRatedMovies(Constants.API_KEY)
            .results
    }

    suspend fun getUpcoming(): List<MovieDto> {
        return api
            .getUpcomingMovies(Constants.API_KEY)
            .results
    }


    suspend fun getMovieDetails(movieId: Int) : MovieDetailsDto {
        return api
            .getMovieDetails(
                movieId,
                Constants.API_KEY
            )
    }


}