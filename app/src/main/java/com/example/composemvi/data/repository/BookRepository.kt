package com.example.composemvi.data.repository

import androidx.paging.PagingData
import com.example.composemvi.data.model.BookDataModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun searchAndCacheBooks(query: String): Flow<PagingData<BookDataModel>>

    suspend fun updateBookmarkStatus(isbn: String, isBookmarked: Boolean)

    suspend fun getAllBooks(): Flow<PagingData<BookDataModel>>

    suspend fun getAllBookmarkedBooks(): Flow<PagingData<BookDataModel>>

    suspend fun getBookByIsbn(isbn: String): BookDataModel

    suspend fun isBookExistsByIsbn(isbn: String): Boolean
}
