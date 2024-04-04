package com.example.moviestmdb.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.databinding.FragmentHomeBinding
import com.example.moviestmdb.ui.adapter.MovieAdapter
import com.example.moviestmdb.ui.adapter.MovieCategory
import com.example.moviestmdb.utils.Constants
import com.example.moviestmdb.utils.Status
import com.example.moviestmdb.model.Movie
import com.example.moviestmdb.utils.Resource


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura los RecyclerViews
        setupRecyclerView(binding.nowPlayingRecyclerView, MovieCategory.NOW_PLAYING)
        setupRecyclerView(binding.popularMoviesRecyclerView, MovieCategory.POPULAR)
        setupRecyclerView(binding.topRatedMoviesRecyclerView, MovieCategory.TOP_RATED)
        setupRecyclerView(binding.upcomingMoviesRecyclerView, MovieCategory.UPCOMING)

        // Carga los datos
        viewModel.getNowPlayingMovies()
        viewModel.getPopularMovies()
        viewModel.getTopRatedMovies()
        viewModel.getUpcomingMovies()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, category: MovieCategory) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= lastVisibleItem + Constants.VISIBLE_THRESHOLD) {
                    when (category) {
                        MovieCategory.NOW_PLAYING -> viewModel.getNowPlayingMovies()
                        MovieCategory.POPULAR -> viewModel.getPopularMovies()
                        MovieCategory.TOP_RATED -> viewModel.getTopRatedMovies()
                        MovieCategory.UPCOMING -> viewModel.getUpcomingMovies()
                    }
                }
            }
        })

        when (category) {
            MovieCategory.NOW_PLAYING -> observeMovies(viewModel.nowPlayingMovies, recyclerView, MovieCategory.NOW_PLAYING)
            MovieCategory.POPULAR -> observeMovies(viewModel.popularMovies, recyclerView, MovieCategory.POPULAR)
            MovieCategory.TOP_RATED -> observeMovies(viewModel.topRatedMovies, recyclerView, MovieCategory.TOP_RATED)
            MovieCategory.UPCOMING -> observeMovies(viewModel.upcomingMovies, recyclerView, MovieCategory.UPCOMING)
        }
    }

    private fun observeMovies(liveData: LiveData<Resource<List<Movie>>>, recyclerView: RecyclerView, category: MovieCategory) {
        liveData.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val movies = resource.data
                    if (movies != null) {
                        val movieAdapter = MovieAdapter(category, movies) { movie ->
                            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movie.id)
                            findNavController().navigate(action)
                        }
                        recyclerView.adapter = movieAdapter
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
