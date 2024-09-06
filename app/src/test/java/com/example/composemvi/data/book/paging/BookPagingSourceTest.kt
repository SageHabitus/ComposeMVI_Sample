package com.example.composemvi.data.book.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.composemvi.data.source.local.entity.BookEntity

class BookPagingSourceTest(private val dummyBooks: List<BookEntity>) : PagingSource<Int, BookEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookEntity> {
        return LoadResult.Page(
            data = dummyBooks,
            prevKey = null,
            nextKey = null,
        )
    }

    override fun getRefreshKey(state: PagingState<Int, BookEntity>): Int? = null
}
