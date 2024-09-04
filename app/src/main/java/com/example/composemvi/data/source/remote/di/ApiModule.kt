package com.example.composemvi.data.source.remote.di

import com.example.composemvi.data.source.remote.api.BookApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideBookApiService(retrofit: Retrofit): BookApi {
        return retrofit.create(BookApi::class.java)
    }
}
