package com.example.moviemvvm.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviemvvm.R
import com.example.moviemvvm.data.api.POSTER_BASE_URL
import com.example.moviemvvm.data.api.TheMovieDBClient
import com.example.moviemvvm.data.api.TheMovieDBInterface
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.data.value_object.MovieDetails
import com.example.moviemvvm.databinding.ActivitySingleMovieBinding
import java.text.NumberFormat
import java.util.Locale

class SingleMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleMovieBinding
    private lateinit var viewModel: SingleMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId: Int = intent.getIntExtra("id", 1)
        initViewModel(movieId)
        initUI()
        initNetworkUI()

    }

    private fun initNetworkUI() {
        viewModel.networkState.observe(this, Observer {
            binding.progressBar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun initUI() {
        viewModel.movieDetails.observe(this) { movieDetails ->
            binding.movieTitle.text = movieDetails.title
            binding.movieTagline.text = movieDetails.tagline
            binding.movieReleaseDate.text = movieDetails.releaseDate
            binding.movieRating.text = movieDetails.rating.toString()
            binding.movieRuntime.text = getString(R.string.minutes, movieDetails.runtime.toString())
            binding.movieOverview.text = movieDetails.overview

            val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
            binding.movieBudget.text = formatCurrency.format(movieDetails.budget)
            binding.movieRevenue.text = formatCurrency.format(movieDetails.revenue)

            val moviePosterURL: String = POSTER_BASE_URL + movieDetails.posterPath
            Glide.with(this)
                .load(moviePosterURL)
                .into(binding.ivMoviePoster)
        }
    }

    private fun initViewModel(movieId: Int) {
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}