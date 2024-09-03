package com.example.composemvi.data.local.book

import android.database.sqlite.SQLiteException
import com.example.composemvi.data.source.local.dao.BookDao
import com.example.composemvi.data.source.local.entity.BookEntity
import com.example.composemvi.data.source.local.exception.LocalDatabaseException
import com.example.composemvi.data.source.local.source.BookLocalDataSourceImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class BookLocalDataSourceImplTest {

    private lateinit var dao: BookDao
    private lateinit var dataSource: BookLocalDataSourceImpl

    @Before
    fun setup() {
        dao = mock(BookDao::class.java)
        dataSource = BookLocalDataSourceImpl(dao)
    }

    @Test
    fun `책을 즐겨찾기 상태로 선택할 수 있다`() = runBlocking {
        val book = BookEntity("123", "Test Book", listOf("Author"), "", "", 1000, 900, "", "", true)
        whenever(dao.selectAllTest(true)).thenReturn(listOf(book))

        val result = dataSource.selectAllTest(true)

        assertEquals(1, result.size)
        assertEquals(book, result.first())
    }

    @Test(expected = LocalDatabaseException::class)
    fun `책을 즐겨찾기 상태로 선택할 때 예외가 발생하면 처리된다`() = runBlocking {
        whenever(dao.selectAllTest(true)).thenThrow(SQLiteException("Test exception"))

        dataSource.selectAllTest(true)
        Unit
    }

    @Test
    fun `책을 ISBN으로 선택할 수 있다`() = runBlocking {
        val book = BookEntity("123", "Test Book", listOf("Author"), "", "", 1000, 900, "", "", true)
        whenever(dao.selectByIsbn("123")).thenReturn(book)

        val result = dataSource.selectBookByIsbn("123")

        assertEquals(book, result)
    }

    @Test(expected = LocalDatabaseException::class)
    fun `책을 ISBN으로 선택할 때 예외가 발생하면 처리된다`() = runBlocking {
        whenever(dao.selectByIsbn("123")).thenThrow(SQLiteException("Test exception"))

        dataSource.selectBookByIsbn("123")
        Unit
    }

    @Test
    fun `책을 삽입하고 예외 없이 처리된다`() = runBlocking {
        val book = BookEntity("123", "Test Book", listOf("Author"), "", "", 1000, 900, "", "", true)

        dataSource.insertBook(book)

        verify(dao).insert(book)
    }

    @Test(expected = LocalDatabaseException::class)
    fun `책을 삽입할 때 예외가 발생하면 처리된다`() = runBlocking {
        val book = BookEntity("123", "Test Book", listOf("Author"), "", "", 1000, 900, "", "", true)
        whenever(dao.insert(book)).thenThrow(SQLiteException("Test exception"))

        dataSource.insertBook(book)
    }

    @Test
    fun `책의 즐겨찾기 상태를 업데이트하고 예외 없이 처리된다`() = runBlocking {
        dataSource.updateBookmarkStatus("123", true)

        verify(dao).updateBookmarkStatus("123", true)
    }

    @Test(expected = LocalDatabaseException::class)
    fun `책의 즐겨찾기 상태를 업데이트할 때 예외가 발생하면 처리된다`() = runBlocking {
        whenever(dao.updateBookmarkStatus("123", true)).thenThrow(SQLiteException("Test exception"))

        dataSource.updateBookmarkStatus("123", true)
    }

    @Test
    fun `책을 삭제하고 예외 없이 처리된다`() = runBlocking {
        dataSource.deleteBookByIsbn("123")

        verify(dao).deleteByIsbn("123")
    }

    @Test(expected = LocalDatabaseException::class)
    fun `책을 삭제할 때 예외가 발생하면 처리된다`() = runBlocking {
        whenever(dao.deleteByIsbn("123")).thenThrow(SQLiteException("Test exception"))

        dataSource.deleteBookByIsbn("123")
    }
}
