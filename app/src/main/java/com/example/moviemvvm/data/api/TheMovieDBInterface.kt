package com.example.moviemvvm.data.api

import com.example.moviemvvm.data.value_object.MovieDetails
import com.example.moviemvvm.data.value_object.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

//    https://api.themoviedb.org/3/movie/popular?api_key=2df59dd4d299eb6e2eaad421aac3ac15&page=1
//    https://api.themoviedb.org/3/movie/299054?api_key=2df59dd4d299eb6e2eaad421aac3ac15

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}