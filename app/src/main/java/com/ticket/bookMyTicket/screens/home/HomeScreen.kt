package com.ticket.bookMyTicket.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ticket.bookMyTicket.screens.common.commonUtils.ErrorView
import com.ticket.bookMyTicket.screens.common.commonUtils.LoadingView
import com.ticket.bookMyTicket.screens.common.homeScreen.MovieHorizontalRow
import com.ticket.bookMyTicket.screens.common.homeScreen.NowPlayingCarousel
import com.ticket.bookMyTicket.screens.common.homeScreen.PopularMoviesGrid
import com.ticket.bookMyTicket.screens.common.commonUtils.SectionTitle
import com.ticket.bookMyTicket.screens.common.homeScreen.UpcomingMovieSection


@Composable
fun HomeScreen(navController: NavController,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val upcomingState by viewModel.upcomingMoviesUIState.collectAsState()
    val topRatedState by viewModel.topRatedMoviesUiState.collectAsState()
    val popularState by viewModel.popularMoviesUiState.collectAsState()
    val nowPlayingState by viewModel.nowPlayingMoviesUiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        item {
            UpcomingSection(upcomingState , navController)
        }

        item {
            NowPlayingSection(nowPlayingState , navController)
        }
        item {
            SectionTitle("Top Rated")
            TopRatedSection(topRatedState , navController)
        }
        item {
            SectionTitle("Popular Movies")
            PopularSection(popularState , navController)
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}


@Composable
fun UpcomingSection(state: HomeUpcomingMoviesUIState , navController: NavController) {

    when(state) {

        is HomeUpcomingMoviesUIState.Loading -> {
            LoadingView()
        }

        is HomeUpcomingMoviesUIState.Error -> {
            ErrorView(state.message)
        }

        is HomeUpcomingMoviesUIState.Success -> {
            UpcomingMovieSection(
                items = state.movies,
                onMovieClick = { movieId ->
                    navController.navigate("details/$movieId")
                }
            )
        }
    }
}



@Composable
fun NowPlayingSection(state: NowPlayingMoviesUiState , navController: NavController) {

    when (state) {

        is NowPlayingMoviesUiState.Loading -> LoadingView()

        is NowPlayingMoviesUiState.Error -> ErrorView(state.message)

        is NowPlayingMoviesUiState.Success -> {
            NowPlayingCarousel(
                movies = state.movies,
                onMovieClick = {
                    navController.navigate("details/${it}")
                }
            )
        }
    }
}


@Composable
fun TopRatedSection(state: TopRatedMoviesUiState , navController: NavController) {

    when (state) {

        is TopRatedMoviesUiState.Loading -> LoadingView()

        is TopRatedMoviesUiState.Error -> ErrorView(state.message)

        is TopRatedMoviesUiState.Success -> {
            MovieHorizontalRow(
                movies = state.movies,
                onMovieClick = {
                    navController.navigate("details/${it}")
                }
            )
        }
    }
}

@Composable
fun PopularSection(state: PopularMoviesUiState , navController: NavController) {

    when (state) {

        is PopularMoviesUiState.Loading -> LoadingView()

        is PopularMoviesUiState.Error -> ErrorView(state.message)

        is PopularMoviesUiState.Success -> {
            PopularMoviesGrid(
                movies = state.movies,
                onMovieClick = { movieId ->
                    navController.navigate("details/$movieId")
                }
            )
        }
    }
}

