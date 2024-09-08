package com.example.composemvi.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.composemvi.data.source.local.entity.BookEntity

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun selectAll(): PagingSource<Int, BookEntity>

    @Query("SELECT * FROM book WHERE isbn = :isbn")
    suspend fun selectByIsbn(isbn: String): BookEntity

    @Query("SELECT * FROM book WHERE is_bookmarked = :isBookmarked")
    fun selectByBookmarked(isBookmarked: Boolean): PagingSource<Int, BookEntity>

    @Query(
        """
    SELECT * FROM book 
    WHERE title LIKE '%' || :query || '%' 
    OR authors LIKE '%' || :query || '%'
    """,
    )
    fun selectBooksByQuery(query: String): PagingSource<Int, BookEntity>

    @Query("SELECT isbn FROM book")
    suspend fun selectBookmarkedIsbns(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<BookEntity>)

    @Upsert
    suspend fun insertOrUpdateBook(entity: BookEntity)

    @Query("UPDATE book SET is_bookmarked = :isBookmarked WHERE isbn = :isbn")
    suspend fun updateBookmarkStatus(isbn: String, isBookmarked: Boolean)

    @Query("SELECT EXISTS(SELECT 1 FROM book WHERE isbn = :isbn)")
    suspend fun isBookExistsByIsbn(isbn: String): Boolean

    @Query("DELETE FROM book WHERE isbn = :isbn")
    suspend fun deleteByIsbn(isbn: String)

    @Query("DELETE FROM book")
    suspend fun deleteAll()

    @Query("DELETE FROM book WHERE is_bookmarked = :isBookmarked")
    suspend fun deleteBooksByBookmarkStatus(isBookmarked: Boolean)

    @Transaction
    suspend fun refreshAndInsertBooks(bookEntitiesFromRemote: List<BookEntity>, isRefreshNeeded: Boolean) {
        if (isRefreshNeeded) {
            deleteBooksByBookmarkStatus(isBookmarked = false)
        }

        val bookmarkedIsbn = selectBookmarkedIsbns().toSet()
        bookEntitiesFromRemote.forEach { entity ->
            val updatedBookEntity = if (bookmarkedIsbn.contains(entity.isbn)) {
                entity.copy(isBookmarked = true)
            } else {
                entity
            }

            insertOrUpdateBook(updatedBookEntity)
        }
    }
}
