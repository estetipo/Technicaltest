package com.carlos.satori.technical_test.data.repository.room

import androidx.lifecycle.LiveData
import com.carlos.satori.technical_test.data.database.DBTest
import com.carlos.satori.technical_test.data.model.Location
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImp @Inject constructor(
    val dbTest: DBTest
) :LocationRepository{
    override suspend fun upsert(location: Location): Long {
        return dbTest.locationDao().upsert(location)
    }

    override fun get(): Flow<List<Location>?> {
        return dbTest.locationDao().get()
    }

    override fun getWithLiveData(): LiveData<List<Location>?> {
        return dbTest.locationDao().getWithLiveData()
    }

    override suspend fun deleteAll() {
        return dbTest.locationDao().deleteAll()
    }
}