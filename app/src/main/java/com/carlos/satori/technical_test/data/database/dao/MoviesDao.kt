package com.carlos.satori.technical_test.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.carlos.satori.technical_test.data.model.movies.Results
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    //Dao for the movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(results: Results): Long

    @Query("SELECT * FROM results")
    fun get(): Flow<List<Results>?>

    @Query("SELECT * FROM results")
    fun getWithLiveData(): LiveData<List<Results>?>

    @Query("DELETE FROM results")
    suspend fun deleteAll()
}