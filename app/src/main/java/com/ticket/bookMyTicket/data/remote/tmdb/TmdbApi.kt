package com.ticket.bookMyTicket.data.remote.tmdb

import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDto
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TmdbApi {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key")apiKey: String): MovieListResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key")apiKey: String): MovieListResponse


    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") apiKey: String): MovieListResponse


    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails (
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") append: String = "credits,videos,reviews"
    ): MovieDetailsDto

}