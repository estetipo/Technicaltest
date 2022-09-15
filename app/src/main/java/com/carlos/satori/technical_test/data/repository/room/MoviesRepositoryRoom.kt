package com.carlos.satori.technical_test.data.repository.room

import androidx.lifecycle.LiveData
import com.carlos.satori.technical_test.data.model.movies.Results
import kotlinx.coroutines.flow.Flow

interface MoviesRepositoryRoom {
    suspend fun upsert(results: Results): Long
    fun get(): Flow<List<Results>?>
    fun getWithLiveData(): LiveData<List<Results>?>
    suspend fun deleteAll()
}