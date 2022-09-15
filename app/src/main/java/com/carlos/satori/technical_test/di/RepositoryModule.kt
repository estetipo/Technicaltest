package com.carlos.satori.technical_test.di

import com.carlos.satori.technical_test.data.repository.online.MoviesRepository
import com.carlos.satori.technical_test.data.repository.online.MoviesRepositoryImp
import com.carlos.satori.technical_test.data.repository.room.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindImagesRepository(imagesRepositoryImp: ImagesRepositoryImp): ImagesRepository
    @Binds
    abstract fun bindLocationRepository(locationRepositoryImp: LocationRepositoryImp): LocationRepository
    @Binds
    abstract fun movieRepository(moviesRepositoryImp: MoviesRepositoryImp): MoviesRepository
    @Binds
    abstract fun movieRepositoryRoom(moviesRepositoryRoomImp: MoviesRepositoryRoomImp): MoviesRepositoryRoom
}