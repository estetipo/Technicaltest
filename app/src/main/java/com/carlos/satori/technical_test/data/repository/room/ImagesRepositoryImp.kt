package com.carlos.satori.technical_test.data.repository.room

import androidx.lifecycle.LiveData
import com.carlos.satori.technical_test.data.model.Images
import kotlinx.coroutines.flow.Flow
import com.carlos.satori.technical_test.data.database.DBTest
import javax.inject.Inject

class ImagesRepositoryImp @Inject constructor(
    val dbTest : DBTest
):ImagesRepository {
    override suspend fun upsert(image: Images): Long {
        return dbTest.imagesDao().upsert(image)
    }

    override fun get(): Flow<List<Images>?> {
        return dbTest.imagesDao().get()
    }

    override fun getWithLiveData(): LiveData<List<Images>?> {
        return dbTest.imagesDao().getWithLiveData()
    }


    override suspend fun deleteAll() {
        return dbTest.imagesDao().deleteAll()
    }


}