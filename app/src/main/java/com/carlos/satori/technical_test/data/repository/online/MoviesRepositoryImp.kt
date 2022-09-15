package com.carlos.satori.technical_test.data.repository.online

import com.carlos.satori.technical_test.api.ApiService
import com.carlos.satori.technical_test.data.model.movies.Movies
import com.carlos.satori.technical_test.util.BaseApiResponse
import com.carlos.satori.technical_test.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepositoryImp @Inject constructor(
    private val apiService: ApiService
):MoviesRepository, BaseApiResponse() {
    override suspend fun getMovies(): Flow<NetworkResult<Movies>> = safeApiCall{
        apiService.getPopularMovies()
    }
}