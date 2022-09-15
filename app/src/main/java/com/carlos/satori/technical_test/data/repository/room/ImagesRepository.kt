package com.carlos.satori.technical_test.data.repository.room

import androidx.lifecycle.LiveData
import com.carlos.satori.technical_test.data.model.Images
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    suspend fun upsert(image: Images): Long
    fun get(): Flow<List<Images>?>
    fun getWithLiveData(): LiveData<List<Images>?>
    suspend fun deleteAll()
}