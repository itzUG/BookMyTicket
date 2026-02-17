package com.ticket.bookMyTicket.screens.common.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDto

@Composable
fun PopularMoviesGrid(
    movies: List<MovieDto>,
    onMovieClick: (Int) -> Unit
) {

    val rows = movies.chunked(3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        rows.forEach { rowMovies ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                rowMovies.forEach { movie ->
                    PopularMovieCard(
                        movie = movie,
                        modifier = Modifier.weight(1f),
                        onMovieClick = {
                            onMovieClick(movie.id)
                        }
                    )
                }

                if (rowMovies.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun PopularMovieCard(
    movie: MovieDto,
    modifier: Modifier = Modifier,
    onMovieClick: () -> Unit
) {

    Card(
        modifier = modifier.clickable{
            onMovieClick()
        },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

