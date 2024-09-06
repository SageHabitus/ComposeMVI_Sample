package com.example.composemvi.data.di

import androidx.paging.PagingConfig
import com.example.composemvi.data.repository.BookRepository
import com.example.composemvi.data.repository.BookRepositoryImpl
import com.example.composemvi.data.source.local.source.BookLocalDataSource
import com.example.composemvi.data.source.remote.source.BookRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideBookRepository(
        bookLocalDataSource: BookLocalDataSource,
        bookRemoteDataSource: BookRemoteDataSource,
        pagingConfig: PagingConfig,
    ): BookRepository {
        return BookRepositoryImpl(
            local = bookLocalDataSource,
            remote = bookRemoteDataSource,
            pagingConfig = pagingConfig,
        )
    }
}
