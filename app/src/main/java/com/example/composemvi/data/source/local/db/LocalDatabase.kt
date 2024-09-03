package com.example.composemvi.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.composemvi.data.source.local.converter.Converter
import com.example.composemvi.data.source.local.dao.BookDao
import com.example.composemvi.data.source.local.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
