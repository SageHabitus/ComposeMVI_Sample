package com.example.composemvi.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composemvi.data.source.local.entity.BookEntity

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun selectAll(): PagingSource<Int, BookEntity>

    @Query("SELECT * FROM book WHERE isbn = :isbn")
    suspend fun selectByIsbn(isbn: String): BookEntity

    @Query("SELECT * FROM book WHERE is_bookmarked = :isBookmarked")
    fun selectByBookmarked(isBookmarked: Boolean): PagingSource<Int, BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<BookEntity>)

    @Query("UPDATE book SET is_bookmarked = :isBookmarked WHERE isbn = :isbn")
    suspend fun updateBookmarkStatus(isbn: String, isBookmarked: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM book WHERE isbn = :isbn)")
    suspend fun isBookExistsByIsbn(isbn: String): Boolean

    @Query("DELETE FROM book WHERE isbn = :isbn")
    suspend fun deleteByIsbn(isbn: String)

    @Query("DELETE FROM book")
    suspend fun deleteAll()
}
