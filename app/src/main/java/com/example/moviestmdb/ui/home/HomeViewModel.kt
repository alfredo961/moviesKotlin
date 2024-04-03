package com.example.moviestmdb.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviestmdb.api.MovieService
import com.example.moviestmdb.model.Movie
import com.example.moviestmdb.repository.MovieRepository
import com.example.moviestmdb.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository(MovieService.tmdbApi)

    private val _nowPlayingMovies = MutableLiveData<Resource<List<Movie>>>()
    val nowPlayingMovies: LiveData<Resource<List<Movie>>> get() = _nowPlayingMovies

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE

    fun getNowPlayingMovies() {
        if (currentPage <= totalPages) {
            viewModelScope.launch {
                _nowPlayingMovies.value = Resource.loading(null)
                val response = repository.getNowPlayingMovies(currentPage)
                if (response.isSuccess) {
                    response.getOrNull()?.let { movieResponse ->
                        totalPages = movieResponse.totalPages
                        currentPage++
                        val oldMovies = _nowPlayingMovies.value?.data ?: emptyList()
                        val newMovies = oldMovies + movieResponse.results
                        _nowPlayingMovies.value = Resource.success(newMovies)
                    }
                } else {
                    _nowPlayingMovies.value = Resource.error(response.exceptionOrNull()?.message ?: "Unknown error", null)
                }
            }
        }
    }

}
