package com.example.composemvi.data.local.book

import android.database.sqlite.SQLiteException
import androidx.paging.PagingSource
import com.example.composemvi.data.book.dummy.TestResourceLoader.BOOK_LOCAL_TEST_JSON
import com.example.composemvi.data.book.dummy.TestResourceLoader.getListFromResource
import com.example.composemvi.data.source.local.dao.BookDao
import com.example.composemvi.data.source.local.entity.BookEntity
import com.example.composemvi.data.source.local.exception.RoomDatabaseException
import com.example.composemvi.data.source.local.source.BookLocalDataSource
import com.example.composemvi.data.source.local.source.BookLocalDataSourceImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class BookLocalDataSourceImplTest {

    private lateinit var dao: BookDao
    private lateinit var dataSource: BookLocalDataSource
    private lateinit var dummyBooks: List<BookEntity>

    @Before
    fun setup() {
        dao = mock(BookDao::class.java)
        dataSource = BookLocalDataSourceImpl(dao)

        dummyBooks = getListFromResource<BookEntity>(BOOK_LOCAL_TEST_JSON)
    }

    @Test
    fun `책을 즐겨찾기 상태로 선택할 수 있다`() = runBlocking {
        val expectedBook = dummyBooks.first { it.isBookmarked }

        val pagingSourceResult = PagingSource.LoadResult.Page<Int, BookEntity>(
            data = listOf(expectedBook),
            prevKey = null,
            nextKey = null,
        )

        val mockPagingSource = mock(PagingSource::class.java) as PagingSource<Int, BookEntity>

        whenever(mockPagingSource.load(any<PagingSource.LoadParams<Int>>()))
            .thenReturn(pagingSourceResult)
        whenever(dao.selectByBookmarked(true)).thenReturn(mockPagingSource)

        val result = dataSource.selectBooksByBookmarked(true)

        verify(dao).selectByBookmarked(true)

        assertEquals(pagingSourceResult.data.first(), expectedBook)
        assertEquals(result, mockPagingSource)
    }

    @Test(expected = RoomDatabaseException::class)
    fun `즐겨찾기 상태 선택 중 예외가 발생하면 처리된다`(): Unit = runBlocking {
        whenever(dao.selectByBookmarked(true)).thenThrow(SQLiteException("Test exception"))

        dataSource.selectBooksByBookmarked(true)
    }

    @Test
    fun `책을 ISBN으로 선택할 수 있다`() = runBlocking {
        val book = dummyBooks.random()
        whenever(dao.selectByIsbn("123")).thenReturn(book)

        val result = dataSource.selectBookByIsbn("123")

        assertEquals(book, result)
    }

    @Test(expected = RoomDatabaseException::class)
    fun `책을 ISBN으로 선택할 때 예외가 발생하면 처리된다`() = runBlocking {
        whenever(dao.selectByIsbn("123")).thenThrow(SQLiteException("Test exception"))

        dataSource.selectBookByIsbn("123")
        Unit
    }

    @Test
    fun `책을 삽입하고 예외 없이 처리된다`() = runBlocking {
        val book = dummyBooks.random()

        dataSource.insertBook(book)

        verify(dao).insert(book)
    }

    @Test(expected = RoomDatabaseException::class)
    fun `책을 삽입할 때 예외가 발생하면 처리된다`() = runBlocking {
        val book = dummyBooks.random()
        whenever(dao.insert(book)).thenThrow(SQLiteException("Test exception"))

        dataSource.insertBook(book)
    }

    @Test
    fun `책의 즐겨찾기 상태를 업데이트하고 예외 없이 처리된다`() = runBlocking {
        dataSource.updateBookmarkStatus("123", true)

        verify(dao).updateBookmarkStatus("123", true)
    }

    @Test(expected = RoomDatabaseException::class)
    fun `책의 즐겨찾기 상태를 업데이트할 때 예외가 발생하면 처리된다`() = runBlocking {
        whenever(dao.updateBookmarkStatus("123", true)).thenThrow(SQLiteException("Test exception"))

        dataSource.updateBookmarkStatus("123", true)
    }

    @Test
    fun `책을 삭제하고 예외 없이 처리된다`() = runBlocking {
        dataSource.deleteBookByIsbn("123")

        verify(dao).deleteByIsbn("123")
    }

    @Test(expected = RoomDatabaseException::class)
    fun `책을 삭제할 때 예외가 발생하면 처리된다`() = runBlocking {
        whenever(dao.deleteByIsbn("123")).thenThrow(SQLiteException("Test exception"))

        dataSource.deleteBookByIsbn("123")
    }
}
