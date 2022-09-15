package com.carlos.satori.technical_test.api

import com.carlos.satori.technical_test.data.model.movies.Movies
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("popular")
    suspend fun getPopularMovies(): Response<Movies>
}