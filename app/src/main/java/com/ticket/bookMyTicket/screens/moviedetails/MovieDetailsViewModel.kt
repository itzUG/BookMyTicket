package com.ticket.bookMyTicket.screens.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto
import com.ticket.bookMyTicket.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _movieDetailsUIState = MutableStateFlow<MovieDetailsFetchingUiState>(MovieDetailsFetchingUiState.Loading(true))
    val movieDetailsUIState = _movieDetailsUIState.asStateFlow()


    fun fetchMovieDetails(movieId : Int) {
        viewModelScope.launch {
            _movieDetailsUIState.value = MovieDetailsFetchingUiState.Loading(true)
            try {
                val movieDetail =  repository.getMovieDetails(movieId = movieId)
                _movieDetailsUIState.value = MovieDetailsFetchingUiState.Success(movieDetail)

            } catch (e : Exception) {
                _movieDetailsUIState.value = MovieDetailsFetchingUiState.Error(e.message ?: " Something went wrong")
            }
        }
    }
}




sealed class MovieDetailsFetchingUiState() {

    data class Loading(val isLoading: Boolean ) : MovieDetailsFetchingUiState()
    data class Success(val movieDetails : MovieDetailsDto) : MovieDetailsFetchingUiState()
    data class Error(val message: String): MovieDetailsFetchingUiState()


}



