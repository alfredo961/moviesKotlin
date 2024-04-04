package com.example.moviestmdb.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviestmdb.api.MovieService
import com.example.moviestmdb.model.Movie
import com.example.moviestmdb.model.MovieResponse
import com.example.moviestmdb.repository.MovieRepository
import com.example.moviestmdb.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository(MovieService.tmdbApi)

    private val _nowPlayingMovies = MutableLiveData<Resource<List<Movie>>>()
    val nowPlayingMovies: LiveData<Resource<List<Movie>>> get() = _nowPlayingMovies

    private val _popularMovies = MutableLiveData<Resource<List<Movie>>>()
    val popularMovies: LiveData<Resource<List<Movie>>> get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<Resource<List<Movie>>>()
    val topRatedMovies: LiveData<Resource<List<Movie>>> get() = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<Resource<List<Movie>>>()
    val upcomingMovies: LiveData<Resource<List<Movie>>> get() = _upcomingMovies

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE

    fun getNowPlayingMovies() {
        getMovies(_nowPlayingMovies) { repository.getNowPlayingMovies(currentPage) }
    }

    fun getPopularMovies() {
        getMovies(_popularMovies) { repository.getPopularMovies(currentPage) }
    }

    fun getTopRatedMovies() {
        getMovies(_topRatedMovies) { repository.getTopRatedMovies(currentPage) }
    }

    fun getUpcomingMovies() {
        getMovies(_upcomingMovies) { repository.getUpcomingMovies(currentPage) }
    }

    private fun getMovies(liveData: MutableLiveData<Resource<List<Movie>>>, getMovies: suspend () -> Result<MovieResponse>) {
        if (currentPage <= totalPages) {
            viewModelScope.launch {
                liveData.value = Resource.loading(liveData.value?.data)
                val response = getMovies()
                if (response.isSuccess) {
                    response.getOrNull()?.let { movieResponse ->
                        totalPages = movieResponse.totalPages
                        currentPage++
                        val oldMovies = liveData.value?.data ?: emptyList()
                        val newMovies = oldMovies + movieResponse.results
                        liveData.value = Resource.success(newMovies)
                    }
                } else {
                    liveData.value = Resource.error(response.exceptionOrNull()?.message ?: "Unknown error", liveData.value?.data)
                }
            }
        }
    }

}
