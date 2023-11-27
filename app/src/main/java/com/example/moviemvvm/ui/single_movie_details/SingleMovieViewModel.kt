package com.example.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviemvvm.data.api.TheMovieDBClient
import com.example.moviemvvm.data.api.TheMovieDBInterface
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.data.value_object.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(
//    private val movieRepository: MovieDetailsRepository,
    movieId: Int
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
    private val movieRepository: MovieDetailsRepository = MovieDetailsRepository(apiService)

    // by lazy -> 클래스가 초기화 되는 시점이 아니라 해당 변수가 사용될 때 초기화 됨
    val movieDetails: LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    // 화면 사라질 때
    override fun onCleared() {
        super.onCleared()
        //메모리 누수 막기 위해
        compositeDisposable.dispose()
    }
}