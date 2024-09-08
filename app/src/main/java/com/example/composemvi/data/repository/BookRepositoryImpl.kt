package com.example.composemvi.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.composemvi.data.exception.DataExceptionMapper
import com.example.composemvi.data.mapper.toDataModel
import com.example.composemvi.data.model.BookDataModel
import com.example.composemvi.data.paging.BookRemoteMediator
import com.example.composemvi.data.source.local.source.BookLocalDataSource
import com.example.composemvi.data.source.remote.source.BookRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val remote: BookRemoteDataSource,
    private val local: BookLocalDataSource,
    private val pagingConfig: PagingConfig,
) : BookRepository {

    private val query = MutableStateFlow("")

    override suspend fun searchAndCacheBooks(query: String): Flow<PagingData<BookDataModel>> {
        this.query.value = query
        return this.query
            .flatMapLatest { currentQuery ->
                Pager(
                    config = pagingConfig,
                    remoteMediator = BookRemoteMediator(currentQuery, remote, local),
                    pagingSourceFactory = { local.selectBookByQuery(currentQuery) },
                ).flow
                    .map { pagingData ->
                        pagingData.map { it.toDataModel() }
                    }
                    .catch { exception ->
                        throw DataExceptionMapper.toDataException(exception)
                    }
            }
    }

    override suspend fun updateBookmarkStatus(isbn: String, isBookmarked: Boolean): Unit = runCatching {
        local.updateBookmarkStatus(isbn, isBookmarked)
    }.getOrElse { exception ->
        throw DataExceptionMapper.toDataException(exception)
    }

    override suspend fun getAllBooks(): Flow<PagingData<BookDataModel>> = Pager(
        config = pagingConfig,
        pagingSourceFactory = { local.selectAllBooks() },
    ).flow
        .map { pagingData ->
            pagingData.map { it.toDataModel() }
        }
        .catch { exception ->
            throw DataExceptionMapper.toDataException(exception)
        }

    override suspend fun getAllBookmarkedBooks(): Flow<PagingData<BookDataModel>> = Pager(
        config = pagingConfig,
        pagingSourceFactory = { local.selectBooksByBookmarked(isBookmarked = true) },
    ).flow
        .map { pagingData ->
            pagingData.map { it.toDataModel() }
        }
        .catch { exception ->
            throw DataExceptionMapper.toDataException(exception)
        }

    override suspend fun getBookByIsbn(isbn: String): BookDataModel = runCatching {
        local.selectBookByIsbn(isbn).toDataModel()
    }.getOrElse { exception ->
        throw DataExceptionMapper.toDataException(exception)
    }

    override suspend fun isBookExistsByIsbn(isbn: String): Boolean = runCatching {
        local.isBookExistsByIsbn(isbn)
    }.getOrElse { exception ->
        throw DataExceptionMapper.toDataException(exception)
    }
}
