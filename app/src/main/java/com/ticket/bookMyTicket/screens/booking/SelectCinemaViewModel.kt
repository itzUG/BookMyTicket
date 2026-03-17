package com.ticket.bookMyTicket.screens.booking

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.ticket.bookMyTicket.data.remote.tmdb.dto.MovieDetailsDto
import com.ticket.bookMyTicket.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectCinemaViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {

    private val _movieDetailsUIState = MutableStateFlow<SelectCinemaViewModelUiState>(SelectCinemaViewModelUiState.Loading(true))
    val movieDetailsUIState = _movieDetailsUIState.asStateFlow()

    private val db by lazy {
        FirebaseFirestore.getInstance()
    }
    private val _cities = MutableStateFlow<List<String>>(emptyList())
    val cities = _cities.asStateFlow()


    fun fetchCities() {
        db.collection("locations")
            .get()
            .addOnSuccessListener { result ->
                _cities.value = result.documents.map { it.id }
            }
    }

    fun fetchMovieDetails(movieId : Int) {
        viewModelScope.launch {
            _movieDetailsUIState.value = SelectCinemaViewModelUiState.Loading(true)
            try {
                val movieDetail =  repository.getMovieDetails(movieId = movieId)
                _movieDetailsUIState.value = SelectCinemaViewModelUiState.Success(movieDetail)

            } catch (e : Exception) {
                _movieDetailsUIState.value = SelectCinemaViewModelUiState.Error(e.message ?: " Something went wrong")
            }
        }
    }
}




sealed class SelectCinemaViewModelUiState() {

    data class Loading(val isLoading: Boolean ) : SelectCinemaViewModelUiState()
    data class Success(val movieDetails : MovieDetailsDto) : SelectCinemaViewModelUiState()
    data class Error(val message: String): SelectCinemaViewModelUiState()


}



