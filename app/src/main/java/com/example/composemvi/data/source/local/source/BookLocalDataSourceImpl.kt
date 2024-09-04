package com.example.composemvi.data.source.local.source

import androidx.paging.PagingSource
import com.example.composemvi.data.source.local.dao.BookDao
import com.example.composemvi.data.source.local.entity.BookEntity
import com.example.composemvi.data.source.local.exception.DatabaseExceptionMapper
import javax.inject.Inject

class BookLocalDataSourceImpl @Inject constructor(
    private val dao: BookDao,
) : BookLocalDataSource {

    override fun selectBooksByBookmarked(isBookmarked: Boolean): PagingSource<Int, BookEntity> = runCatching {
        dao.selectByBookmarked(isBookmarked)
    }.getOrElse { exception ->
        throw DatabaseExceptionMapper.toException(exception)
    }

    override fun selectAllBooks(): PagingSource<Int, BookEntity> = runCatching {
        dao.selectAll()
    }.getOrElse { exception ->
        throw DatabaseExceptionMapper.toException(exception)
    }

    override suspend fun selectBookByIsbn(isbn: String): BookEntity = runCatching {
        dao.selectByIsbn(isbn)
    }.getOrElse { exception ->
        throw DatabaseExceptionMapper.toException(exception)
    }

    override suspend fun insertBook(entity: BookEntity) = runCatching {
        dao.insert(entity)
    }.getOrElse { exception ->
        throw DatabaseExceptionMapper.toException(exception)
    }

    override suspend fun insertBooks(entities: List<BookEntity>) = runCatching {
        dao.insertAll(entities)
    }.getOrElse { exception ->
        throw DatabaseExceptionMapper.toException(exception)
    }

    override suspend fun updateBookmarkStatus(isbn: String, isBookmarked: Boolean) = runCatching {
        dao.updateBookmarkStatus(isbn, isBookmarked)
    }.getOrElse { exception ->
        throw DatabaseExceptionMapper.toException(exception)
    }

    override suspend fun isBookExistsByIsbn(isbn: String): Boolean = runCatching {
        dao.isBookExistsByIsbn(isbn)
    }.getOrElse { exception ->
        throw DatabaseExceptionMapper.toException(exception)
    }

    override suspend fun deleteAllBooks() = runCatching {
        dao.deleteAll()
    }.getOrElse { exception ->
        throw DatabaseExceptionMapper.toException(exception)
    }

    override suspend fun deleteBookByIsbn(isbn: String) = runCatching {
        dao.deleteByIsbn(isbn)
    }.getOrElse { exception ->
        throw DatabaseExceptionMapper.toException(exception)
    }
}
