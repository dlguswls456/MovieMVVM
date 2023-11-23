package com.example.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviemvvm.data.api.FIRST_PAGE
import com.example.moviemvvm.data.api.TheMovieDBInterface
import com.example.moviemvvm.data.value_object.Movie
import com.example.moviemvvm.data.value_object.MovieResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

//페이지 넘버에 따라 화면을 보여줘야 하기 때문
class MovieDataSource(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    lateinit var movieResponse: MovieResponse

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.movieList, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    }, {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        TODO("Not yet implemented")
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.movieList, null, page + 1)
                        networkState.postValue(NetworkState.LOADED)
                    }, {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message.toString())
                    }
                )
        )
    }

}


//class MovieDataSource(
//    private val apiService: TheMovieDBInterface,
//    private val compositeDisposable: CompositeDisposable
//) : PagingSource<Int, Movie>() {
//
//    private var page = FIRST_PAGE
//
//    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
//
//    lateinit var movieResponse: MovieResponse
//
//    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
//        val position = params.key ?: FIRST_PAGE
//        var nextKey: Int? = null
//        networkState.postValue(NetworkState.LOADING)
//        try {
//            compositeDisposable.add(
//                apiService.getPopularMovie(page)
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(
//                        {
//                            networkState.postValue(NetworkState.LOADED)
//                            movieResponse = it
//                            nextKey = if (it.totalPages >= position) {
//                                position + 1
//                            } else {
//                                null
//                            }
//                        }, {
//                            networkState.postValue(NetworkState.ERROR)
//                            Log.e("MovieDataSource", it.message.toString())
//                        }
//                    )
//            )
//            return LoadResult.Page(
//                data = movieResponse.movieList,
//                prevKey = if (position == FIRST_PAGE) null else position - 1,
//                nextKey = nextKey
//            )
//        } catch (e: Exception) {
//            Log.e("MovieDataSource", e.message.toString())
//            return LoadResult.Error(e)
//        }
//
//    }
//}