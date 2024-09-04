package com.example.composemvi.data.local.book

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.composemvi.data.source.local.dao.BookDao
import com.example.composemvi.data.source.local.db.LocalDatabase
import com.example.composemvi.data.source.local.entity.BookEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@SmallTest
class BookDaoTest {

    private lateinit var database: LocalDatabase
    private lateinit var dao: BookDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDatabase::class.java,
        ).allowMainThreadQueries().build()

        dao = database.bookDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `책을 삽입하고 다시 불러올 수 있다`() = runBlocking {
        val book = BookEntity(
            isbn = "123",
            title = "Test Book",
            authors = listOf("Author"),
            contents = "",
            publisher = "",
            price = 1000,
            salePrice = 900,
            thumbnail = "",
            url = "",
            isBookmarked = true,
        )
        dao.insert(book)

        val result = dao.selectByIsbn("123")

        assertEquals(book, result)
    }

    @Test
    fun `책을 여러개 삽입하고 다시 불러올 수 있다`() = runBlocking {
        val books = listOf(
            BookEntity("123", "Test Book 1", listOf("Author 1"), "", "", 1000, 900, "", "", true),
            BookEntity("456", "Test Book 2", listOf("Author 2"), "", "", 2000, 1800, "", "", false),
        )
        dao.insertAll(books)

        val result1 = dao.selectByIsbn("123")
        val result2 = dao.selectByIsbn("456")

        assertEquals(books[0], result1)
        assertEquals(books[1], result2)
    }

    @Test
    fun `즐겨찾기 상태로 책들을 불러올 수 있다`() = runBlocking {
        val books = listOf(
            BookEntity("123", "Test Book 1", listOf("Author 1"), "", "", 1000, 900, "", "", true),
            BookEntity("456", "Test Book 2", listOf("Author 2"), "", "", 2000, 1800, "", "", false),
            BookEntity("789", "Test Book 3", listOf("Author 3"), "", "", 3000, 2700, "", "", true),
        )
        dao.insertAll(books)

        val bookmarkedBooks = dao.selectAllTest(true)

        assertEquals(2, bookmarkedBooks.size)
        assertTrue(bookmarkedBooks.contains(books[0]))
        assertTrue(bookmarkedBooks.contains(books[2]))
    }

    @Test
    fun `즐겨찾기 상태로 페이징된 책들을 불러올 수 있다`() = runBlocking {
        val books = (1..20).map {
            BookEntity(
                isbn = it.toString(),
                title = "Test Book $it",
                authors = listOf("Author $it"),
                contents = "",
                publisher = "",
                price = 1000 * it,
                salePrice = 900 * it,
                thumbnail = "",
                url = "",
                isBookmarked = it % 2 == 0,
            )
        }
        dao.insertAll(books)

        val pagingSource = dao.selectByBookmarked(true)
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false,
            ),
        ) as PagingSource.LoadResult.Page

        assertEquals(10, loadResult.data.size)
        assertTrue(loadResult.data.all { it.isBookmarked })
    }

    @Test
    fun `모든 책들을 페이징된 리스트로 불러올 수 있다`() = runBlocking {
        val books = (1..20).map {
            BookEntity(
                isbn = it.toString(),
                title = "Test Book $it",
                authors = listOf("Author $it"),
                contents = "",
                publisher = "",
                price = 1000 * it,
                salePrice = 900 * it,
                thumbnail = "",
                url = "",
                isBookmarked = it % 2 == 0,
            )
        }
        dao.insertAll(books)

        val pagingSource = dao.selectAll()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false,
            ),
        ) as PagingSource.LoadResult.Page

        assertEquals(10, loadResult.data.size)
    }

    @Test
    fun `책의 즐겨찾기 상태를 업데이트할 수 있다`() = runBlocking {
        val book = BookEntity(
            isbn = "123",
            title = "Test Book",
            authors = listOf("Author"),
            contents = "",
            publisher = "",
            price = 1000,
            salePrice = 900,
            thumbnail = "",
            url = "",
            isBookmarked = false,
        )
        dao.insert(book)

        dao.updateBookmarkStatus("123", true)

        val updatedBook = dao.selectByIsbn("123")
        assertEquals(true, updatedBook.isBookmarked)
    }

    @Test
    fun `책이 존재하는지 확인할 수 있다`() = runBlocking {
        val book = BookEntity(
            isbn = "123",
            title = "Test Book",
            authors = listOf("Author"),
            contents = "",
            publisher = "",
            price = 1000,
            salePrice = 900,
            thumbnail = "",
            url = "",
            isBookmarked = true,
        )
        dao.insert(book)

        val exists = dao.isBookExistsByIsbn("123")
        val notExists = dao.isBookExistsByIsbn("999")

        assertTrue(exists)
        assertFalse(notExists)
    }

    @Test
    fun `책을 삭제할 수 있다`() = runBlocking {
        val book = BookEntity(
            isbn = "123",
            title = "Test Book",
            authors = listOf("Author"),
            contents = "",
            publisher = "",
            price = 1000,
            salePrice = 900,
            thumbnail = "",
            url = "",
            isBookmarked = true,
        )
        dao.insert(book)

        dao.deleteByIsbn("123")

        val result = dao.selectByIsbn("123")
        assertNull(result)
    }

    @Test
    fun `모든 책을 삭제할 수 있다`() = runBlocking {
        val books = listOf(
            BookEntity("123", "Test Book 1", listOf("Author 1"), "", "", 1000, 900, "", "", true),
            BookEntity("456", "Test Book 2", listOf("Author 2"), "", "", 2000, 1800, "", "", false),
        )
        dao.insertAll(books)

        dao.deleteAll()

        val result1 = dao.selectByIsbn("123")
        val result2 = dao.selectByIsbn("456")

        assertNull(result1)
        assertNull(result2)
    }
}
