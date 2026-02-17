package com.ticket.bookMyTicket.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDto
import com.ticket.bookMyTicket.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    // Upcoming

    private val _upcomingMoviesUIState = MutableStateFlow<HomeUpcomingMoviesUIState>(HomeUpcomingMoviesUIState.Loading(true))
    val upcomingMoviesUIState = _upcomingMoviesUIState.asStateFlow()

    // Top Rated
    private val _topRateMoviesUiState =
        MutableStateFlow<TopRatedMoviesUiState>(
            TopRatedMoviesUiState.Loading(true)
        )
    val topRatedMoviesUiState =
        _topRateMoviesUiState.asStateFlow()

    // Popular
    private val _popularMoviesUiState =
        MutableStateFlow<PopularMoviesUiState>(
            PopularMoviesUiState.Loading(true)
        )
    val popularMoviesUiState =
        _popularMoviesUiState.asStateFlow()

    // Now Playing
    private val _nowPlayingMoviesUiState =
        MutableStateFlow<NowPlayingMoviesUiState>(
            NowPlayingMoviesUiState.Loading(true)
        )
    val nowPlayingMoviesUiState =
        _nowPlayingMoviesUiState.asStateFlow()

    init {
        loadUpcomingMovies()
        loadTopMovies()
        loadPopularMovies()
        loadNowPlayingMovies()
    }

    // Upcoming Movies
    private fun loadUpcomingMovies() {
        viewModelScope.launch {
            _upcomingMoviesUIState.value = HomeUpcomingMoviesUIState.Loading(isLoading = true)

            try {
                val movies = repository.getUpcoming()
                _upcomingMoviesUIState.value = HomeUpcomingMoviesUIState.Success(movies)
            } catch (e: Exception) {
                _upcomingMoviesUIState.value = HomeUpcomingMoviesUIState.Error(
                    e.message ?: "Something Went Wrong"
                )
            }
        }
    }

    // Top Rated Movies
    private fun loadTopMovies() {
        viewModelScope.launch {

            _topRateMoviesUiState.value =
                TopRatedMoviesUiState.Loading(true)

            try {
                val movies = repository.getTopRated()

                _topRateMoviesUiState.value =
                    TopRatedMoviesUiState.Success(movies)

            } catch (e: Exception) {
                _topRateMoviesUiState.value =
                    TopRatedMoviesUiState.Error(
                        e.message ?: "Something Went Wrong"
                    )
            }
        }
    }

    // Popular Movies
    private fun loadPopularMovies() {
        viewModelScope.launch {

            _popularMoviesUiState.value =
                PopularMoviesUiState.Loading(true)

            try {
                val movies = repository.getPopular()

                _popularMoviesUiState.value =
                    PopularMoviesUiState.Success(movies)

            } catch (e: Exception) {
                _popularMoviesUiState.value =
                    PopularMoviesUiState.Error(
                        e.message ?: "Something Went Wrong"
                    )
            }
        }
    }

    // Now Playing Movies
    private fun loadNowPlayingMovies() {
        viewModelScope.launch {

            _nowPlayingMoviesUiState.value =
                NowPlayingMoviesUiState.Loading(true)

            try {
                val movies = repository.getNowPlaying()

                _nowPlayingMoviesUiState.value =
                    NowPlayingMoviesUiState.Success(movies)

            } catch (e: Exception) {
                _nowPlayingMoviesUiState.value =
                    NowPlayingMoviesUiState.Error(
                        e.message ?: "Something Went Wrong"
                    )
            }
        }
    }
}

sealed class HomeUpcomingMoviesUIState {

    data class Loading(val isLoading: Boolean) : HomeUpcomingMoviesUIState()

    data class Success(val movies: List<MovieDto>) : HomeUpcomingMoviesUIState()

    data class Error(val message: String) : HomeUpcomingMoviesUIState()

}



sealed class TopRatedMoviesUiState {

    data class Loading(val isLoading: Boolean) : TopRatedMoviesUiState()

    data class Success(val movies: List<MovieDto>) : TopRatedMoviesUiState()

    data class Error(val message: String) : TopRatedMoviesUiState()
}


sealed class PopularMoviesUiState {

    data class Loading(val isLoading: Boolean) : PopularMoviesUiState()

    data class Success(val movies: List<MovieDto>) : PopularMoviesUiState()

    data class Error(val message: String) : PopularMoviesUiState()
}


sealed class NowPlayingMoviesUiState {

    data class Loading(val isLoading: Boolean) : NowPlayingMoviesUiState()

    data class Success(val movies: List<MovieDto>) : NowPlayingMoviesUiState()

    data class Error(val message: String) : NowPlayingMoviesUiState()
}

