package com.example.moviestmdb.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestmdb.databinding.FragmentHomeBinding
import com.example.moviestmdb.ui.adapter.MovieAdapter
import com.example.moviestmdb.utils.Constants
import com.example.moviestmdb.utils.Status
import com.example.moviestmdb.R.navigation.nav_graph


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
        binding.nowPlayingRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Agrega el listener de scroll
        binding.nowPlayingRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= lastVisibleItem + Constants.VISIBLE_THRESHOLD) {
                    viewModel.getNowPlayingMovies()
                }
            }
        })

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val movies = resource.data
                    if (movies != null) {
                        val movieAdapter = MovieAdapter(movies) { movie ->
                            //val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movie.id)
                            //findNavController().navigate(action)



                        }
                        binding.nowPlayingRecyclerView.adapter = movieAdapter
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    // Maneja el error
                }
            }
        }

        // Carga los datos
        viewModel.getNowPlayingMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
