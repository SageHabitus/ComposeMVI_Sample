package com.example.composemvi.data.source.local.di

import android.content.Context
import androidx.room.Room
import com.example.composemvi.data.source.local.dao.BookDao
import com.example.composemvi.data.source.local.db.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "local_database",
        ).build()
    }

    @Singleton
    @Provides
    fun provideBookDao(database: LocalDatabase): BookDao {
        return database.bookDao()
    }
}
