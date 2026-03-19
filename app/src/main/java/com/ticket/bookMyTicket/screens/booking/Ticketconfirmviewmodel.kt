package com.ticket.bookMyTicket.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto
import com.ticket.bookMyTicket.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class TicketConfirmUiState {
    object Loading : TicketConfirmUiState()
    data class Success(val movieDetails: MovieDetailsDto) : TicketConfirmUiState()
    data class Error(val message: String) : TicketConfirmUiState()
}

class TicketConfirmViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<TicketConfirmUiState>(TicketConfirmUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = TicketConfirmUiState.Loading
            try {
                val details = repository.getMovieDetails(movieId)
                _uiState.value = TicketConfirmUiState.Success(details)
            } catch (e: Exception) {
                _uiState.value = TicketConfirmUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}