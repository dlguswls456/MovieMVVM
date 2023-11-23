package com.example.moviemvvm.ui.popular_movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviemvvm.data.api.TheMovieDBClient
import com.example.moviemvvm.data.api.TheMovieDBInterface
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.databinding.ActivityMainBinding
import com.example.moviemvvm.ui.single_movie_details.SingleMovieActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    lateinit var moviePagedListRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()

        moviePagedListRepository = MoviePagedListRepository(apiService)

        viewModel = getViewModel()

        val moviePagedListAdapter = PopularMoviePagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = moviePagedListAdapter.getItemViewType(position)
                return if (viewType == moviePagedListAdapter.MOVIE_VIEW_TYPE) {
                    1
                } else {
                    3
                }
            }
        }

        binding.rvMovieList.layoutManager = gridLayoutManager
        binding.rvMovieList.setHasFixedSize(true)
        binding.rvMovieList.adapter = moviePagedListAdapter

        viewModel.moviePagedList.observe(this, Observer {
            moviePagedListAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.progressBarPopular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtErrorPopular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                moviePagedListAdapter.setNetworkState(it)
            }
        })

    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainActivityViewModel(moviePagedListRepository) as T
            }
        })[MainActivityViewModel::class.java]

    }
}