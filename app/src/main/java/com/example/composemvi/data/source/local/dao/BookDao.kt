package com.example.composemvi.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("DELETE FROM book WHERE is_bookmarked = :isBookmarked")
    suspend fun deleteBooksByBookmarkStatus(isBookmarked: Boolean)

    @Transaction
    suspend fun refreshAndInsertBooks(bookEntities: List<BookEntity>, isRefreshNeeded: Boolean) {
        if (isRefreshNeeded) {
            deleteBooksByBookmarkStatus(isBookmarked = false)
        }

        insertOrUpdateBooks(bookEntities)
    }

    @Query(
        """
    INSERT OR REPLACE INTO book (isbn, title, authors, contents, publisher, price, sale_price, thumbnail, url, 
    is_bookmarked)
    VALUES (:isbn, :title, :authors, :contents, :publisher, :price, :salePrice, :thumbnail, :url, 
        CASE 
            WHEN (SELECT is_bookmarked FROM book WHERE isbn = :isbn) = 1 THEN 1 
            ELSE :isBookmarked 
        END
    )
""",
    )
    suspend fun insertOrUpdateBook(
        isbn: String,
        title: String,
        authors: List<String>,
        contents: String,
        publisher: String,
        price: Int,
        salePrice: Int,
        thumbnail: String,
        url: String,
        isBookmarked: Boolean,
    )

    private suspend fun insertOrUpdateBooks(bookEntities: List<BookEntity>) {
        bookEntities.forEach { book ->
            insertOrUpdateBook(
                isbn = book.isbn,
                title = book.title,
                authors = book.authors,
                contents = book.contents,
                publisher = book.publisher,
                price = book.price,
                salePrice = book.salePrice,
                thumbnail = book.thumbnail,
                url = book.url,
                isBookmarked = book.isBookmarked,
            )
        }
    }
}
