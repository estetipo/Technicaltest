package com.carlos.satori.technical_test.data.repository.online

import com.carlos.satori.technical_test.data.model.movies.Movies
import com.carlos.satori.technical_test.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getMovies(): Flow<NetworkResult<Movies>>
}