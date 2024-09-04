package com.example.composemvi.data.source.local.di

import com.example.composemvi.data.source.local.source.BookLocalDataSource
import com.example.composemvi.data.source.local.source.BookLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindBookLocalDataSource(bookLocalDataSource: BookLocalDataSourceImpl): BookLocalDataSource
}
