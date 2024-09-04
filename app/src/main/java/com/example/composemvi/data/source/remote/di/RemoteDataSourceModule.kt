package com.example.composemvi.data.source.remote.di

import com.example.composemvi.data.source.remote.source.BookRemoteDataSource
import com.example.composemvi.data.source.remote.source.BookRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindBookRemoteDataSource(bookRemoteDataSourceImpl: BookRemoteDataSourceImpl): BookRemoteDataSource
}
