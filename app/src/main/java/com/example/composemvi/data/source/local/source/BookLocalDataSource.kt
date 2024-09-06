package com.example.composemvi.data.source.local.source

import androidx.paging.PagingSource
import com.example.composemvi.data.source.local.entity.BookEntity

interface BookLocalDataSource {

    fun selectBooksByBookmarked(isBookmarked: Boolean): PagingSource<Int, BookEntity>

    fun selectAllBooks(): PagingSource<Int, BookEntity>

    suspend fun selectBookByIsbn(isbn: String): BookEntity

    suspend fun insertBook(entity: BookEntity)

    suspend fun updateBookmarkStatus(isbn: String, isBookmarked: Boolean)

    suspend fun insertBooks(entities: List<BookEntity>)

    suspend fun isBookExistsByIsbn(isbn: String): Boolean

    suspend fun deleteBookByIsbn(isbn: String)

    suspend fun deleteAllBooks()

    suspend fun deleteBooksByBookmarkStatus(isBookmarked: Boolean)

    suspend fun refreshAndInsertBooks(bookEntities: List<BookEntity>, isRefresh: Boolean)
}
