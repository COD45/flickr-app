package com.szoldapps.flickrhometask.app.di

import com.szoldapps.flickrhometask.data.FlickrRepository
import com.szoldapps.flickrhometask.data.FlickrRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DomainModule {

    @Binds
    abstract fun bindFlickrRepository(flickrRepositoryImpl: FlickrRepositoryImpl): FlickrRepository
}
