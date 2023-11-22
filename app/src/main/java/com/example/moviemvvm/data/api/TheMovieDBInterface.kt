package com.example.moviemvvm.data.api

import com.example.moviemvvm.data.value_object.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBInterface {

//    https://api.themoviedb.org/3/movie/popular
//    https://api.themoviedb.org/3/movie/299054

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}