package com.ticket.bookMyTicket.screens.common.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDto

@Composable
fun MovieHorizontalRow(
    movies: List<MovieDto>,
    onMovieClick:(Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(movies) { movie ->
            MoviePosterCard(
                movie,
                onMovieClick = {
                    onMovieClick(movie.id)
                }
            )
        }
    }
}


@Composable
fun MoviePosterCard(movie: MovieDto , onMovieClick: (Int) -> Unit) {

    Card(
        modifier = Modifier
            .padding(end = 12.dp)
            .width(120.dp)
            .height(180.dp).clickable{
                onMovieClick(movie.id)
            }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize()
        )
    }
}