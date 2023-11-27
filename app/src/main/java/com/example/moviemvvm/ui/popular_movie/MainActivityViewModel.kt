package com.example.moviemvvm.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviemvvm.data.api.TheMovieDBClient
import com.example.moviemvvm.data.api.TheMovieDBInterface
import com.example.moviemvvm.data.repository.NetworkState
import com.example.moviemvvm.data.value_object.Movie
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
    private val movieRepository = MoviePagedListRepository(apiService)

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}