package com.carlos.satori.technical_test.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.carlos.satori.technical_test.data.model.Images
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {
    //Dao for the images
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(image: Images): Long

    @Query("SELECT * FROM images")
    fun get(): Flow<List<Images>?>

    @Query("SELECT * FROM images")
    fun getWithLiveData(): LiveData<List<Images>?>

    @Query("DELETE FROM images")
    suspend fun deleteAll()
}