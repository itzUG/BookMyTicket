package com.ticket.bookMyTicket.screens.moviedetails

import android.util.Log
import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto
import com.ticket.bookMyTicket.screens.common.commonUtils.CastCrew
import com.ticket.bookMyTicket.screens.common.commonUtils.CrewSection
import com.ticket.bookMyTicket.screens.common.commonUtils.ErrorView
import com.ticket.bookMyTicket.screens.common.commonUtils.GenresView
import com.ticket.bookMyTicket.screens.common.commonUtils.LoadingView
import com.ticket.bookMyTicket.screens.common.commonUtils.ReviewSection
import com.ticket.bookMyTicket.screens.common.commonUtils.VideoSectionModern
import com.ticket.bookMyTicket.screens.common.movieDetailsScreen.HeaderSection

@Composable
fun MovieDetailsScreen(
    navController: NavController,
    movieId: Int,
    viewModel: MovieDetailsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val movieDetailsUIState by viewModel.movieDetailsUIState.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black.copy(alpha = 1f))
    ) {
        MovieDetailsLazyScreen(navController,movieId,movieDetailsUIState)
    }
}


@Composable
fun MovieDetailsLazyScreen(
    navController: NavController,
    movieId: Int,
    movieDetailsState: MovieDetailsFetchingUiState
) {

    when (movieDetailsState) {
        is MovieDetailsFetchingUiState.Error -> {
            ErrorView(movieDetailsState.message)
        }
        is MovieDetailsFetchingUiState.Loading -> {
            LoadingView()
        }
        is MovieDetailsFetchingUiState.Success -> {
            val movie = movieDetailsState.movieDetails
            DetailScreenUI(movie , navController)
        }
    }
}

@Composable
fun DetailScreenUI(movie: MovieDetailsDto , navController: NavController) {

    Log.d("POPULAR", "Movies: ${movie.credits}")


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            HeaderSection(movie)
            Spacer(modifier = Modifier.height(12.dp))
            movie.genres?.let { genres ->
                if (genres.isNotEmpty()) {
                    GenresView(genres)
                }
            }
            movie.credits?.cast?.let { castList ->
                if (castList.isNotEmpty()) {
                    CastCrew(castList)
                }
            }
            //CrewSection(movie.credits.crew)
            movie.reviews?.results?.let { reviews ->
                if (reviews.isNotEmpty()) {
                    ReviewSection(reviews)
                }
            }
            movie.videos?.results?.let { videos ->
                if (videos.isNotEmpty()) {
                    VideoSectionModern(videos)
                }
            }

            androidx.compose.material3.Button(
                onClick = {
                    navController.navigate("booking/${movie.id}")
                }
            ) {
                Text("Book Now")
            }

            Spacer(modifier = Modifier.padding(bottom = 80.dp))
        }

    }
//Apple
}