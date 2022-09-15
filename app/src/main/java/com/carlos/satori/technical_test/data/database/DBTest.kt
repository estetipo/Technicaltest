package com.carlos.satori.technical_test.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.carlos.satori.technical_test.data.database.converters.DBTypeConverters
import com.carlos.satori.technical_test.data.database.dao.ImagesDao
import com.carlos.satori.technical_test.data.database.dao.LocationDao
import com.carlos.satori.technical_test.data.database.dao.MoviesDao
import com.carlos.satori.technical_test.data.model.Images
import com.carlos.satori.technical_test.data.model.Location
import com.carlos.satori.technical_test.data.model.movies.Dates
import com.carlos.satori.technical_test.data.model.movies.Movies
import com.carlos.satori.technical_test.data.model.movies.Results

@Database(
    entities = [Images::class, Location::class,Results::class],
    version = 1
)
@TypeConverters(DBTypeConverters::class)
abstract class DBTest : RoomDatabase() {

    abstract fun imagesDao(): ImagesDao
    abstract fun locationDao(): LocationDao
    abstract fun moviesDao(): MoviesDao

    companion object {
        @JvmStatic
        fun newInstance(context: Context): DBTest {
            return Room.databaseBuilder(context, DBTest::class.java, "DBTest")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}