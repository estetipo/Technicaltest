package com.carlos.satori.technical_test.data.repository.room

import androidx.lifecycle.LiveData
import com.carlos.satori.technical_test.data.database.DBTest
import com.carlos.satori.technical_test.data.model.movies.Movies
import com.carlos.satori.technical_test.data.model.movies.Results
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepositoryRoomImp @Inject constructor(
    val dbTest: DBTest
) : MoviesRepositoryRoom{
    override suspend fun upsert(results: Results): Long {
        return dbTest.moviesDao().upsert(results)
    }

    override fun get(): Flow<List<Results>?> {
        return dbTest.moviesDao().get()
    }

    override fun getWithLiveData(): LiveData<List<Results>?> {
        return dbTest.moviesDao().getWithLiveData()
    }

    override suspend fun deleteAll() {
        return dbTest.moviesDao().deleteAll()
    }

}