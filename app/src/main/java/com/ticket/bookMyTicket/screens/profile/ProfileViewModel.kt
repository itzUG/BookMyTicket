package com.ticket.bookMyTicket.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ticket.bookMyTicket.data.db.BookedTicketEntity
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto
import com.ticket.bookMyTicket.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class TicketWithMovie(
    val ticket: BookedTicketEntity,
    val movie: MovieDetailsDto?
)

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val items: List<TicketWithMovie>) : ProfileUiState()
    object Empty : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository()
    private val dao = DatabaseProvider.getDatabase(application).bookedTicketDao()

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init { loadTickets() }

    fun loadTickets() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val tickets = withContext(Dispatchers.IO) { dao.getAllTickets() }
                if (tickets.isEmpty()) {
                    _uiState.value = ProfileUiState.Empty
                    return@launch
                }
                val items = tickets.map { ticket ->
                    async {
                        val movie = try { repository.getMovieDetails(ticket.movieId) } catch (e: Exception) { null }
                        TicketWithMovie(ticket, movie)
                    }
                }.awaitAll()
                _uiState.value = ProfileUiState.Success(items)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}