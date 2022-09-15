package com.carlos.satori.technical_test.data.repository.room

import androidx.lifecycle.LiveData
import com.carlos.satori.technical_test.data.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun upsert(location: Location): Long
    fun get(): Flow<List<Location>?>
    fun getWithLiveData(): LiveData<List<Location>?>
    suspend fun deleteAll()
}