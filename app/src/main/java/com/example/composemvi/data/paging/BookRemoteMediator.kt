package com.example.composemvi.data.paging

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.composemvi.data.exception.DataExceptionMapper
import com.example.composemvi.data.mapper.toEntityModel
import com.example.composemvi.data.source.local.entity.BookEntity
import com.example.composemvi.data.source.local.source.BookLocalDataSource
import com.example.composemvi.data.source.remote.source.BookRemoteDataSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class BookRemoteMediator(
    private val query: String,
    private val remote: BookRemoteDataSource,
    private val local: BookLocalDataSource,
) : RemoteMediator<Int, BookEntity>() {

    private var currentPage = 1
    private val mutex = Mutex()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, BookEntity>): MediatorResult =
        mutex.withLock {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = 1
                    1
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> currentPage + 1
            }
            return runCatching {
                val bookResponseModel = remote.searchBooks(query, page, state.config.pageSize)
                val bookEntities = bookResponseModel.documents.map { it.toEntityModel() }

                local.refreshAndInsertBooks(bookEntities, loadType == LoadType.REFRESH)

                currentPage = page

                MediatorResult.Success(endOfPaginationReached = bookResponseModel.meta.isEnd)
            }.getOrElse { exception ->
                MediatorResult.Error((DataExceptionMapper.toDataException(exception)))
            }
        }

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH
}
