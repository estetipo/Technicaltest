package com.carlos.satori.technical_test.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.carlos.satori.technical_test.data.model.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(location: Location): Long

    @Query("SELECT * FROM location")
    fun get(): Flow<List<Location>?>

    @Query("SELECT * FROM location")
    fun getWithLiveData(): LiveData<List<Location>?>

    @Query("DELETE FROM location")
    suspend fun deleteAll()
}