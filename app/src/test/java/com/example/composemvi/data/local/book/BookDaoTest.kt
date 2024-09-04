package com.example.composemvi.data.local.book

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.composemvi.data.source.local.dao.BookDao
import com.example.composemvi.data.source.local.db.LocalDatabase
import com.example.composemvi.data.source.local.entity.BookEntity
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
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
    private lateinit var dummyBooks: List<BookEntity>

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDatabase::class.java,
        ).allowMainThreadQueries().build()

        dao = database.bookDao()

        dummyBooks = loadBookEntitiesFromJson("dummy_books_local.json")
    }

    private fun loadBookEntitiesFromJson(fileName: String): List<BookEntity> {
        val jsonString = javaClass.classLoader?.getResourceAsStream(fileName)?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalArgumentException("Could not read file: $fileName")

        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString(jsonString)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `책을 삽입하고 다시 불러올 수 있다`() = runBlocking {
        val book = dummyBooks[0]
        dao.insert(book)

        val result = dao.selectByIsbn(book.isbn)

        assertEquals(book, result)
    }

    @Test
    fun `책을 여러개 삽입하고 다시 불러올 수 있다`() = runBlocking {
        dao.insertAll(dummyBooks)

        val result1 = dao.selectByIsbn(dummyBooks[0].isbn)
        val result2 = dao.selectByIsbn(dummyBooks[1].isbn)

        assertEquals(dummyBooks[0], result1)
        assertEquals(dummyBooks[1], result2)
    }

    @Test
    fun `즐겨찾기 상태로 책들을 불러올 수 있다`() = runBlocking {
        dao.insertAll(dummyBooks)

        val bookmarkedBooks = dao.selectByBookmarked(true).load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 50,
                placeholdersEnabled = false,
            ),
        ) as PagingSource.LoadResult.Page

        val expectedBooks = dummyBooks.filter { it.isBookmarked }
        assertEquals(expectedBooks.size, bookmarkedBooks.data.size)
        assertTrue(bookmarkedBooks.data.containsAll(expectedBooks))
    }

    @Test
    fun `즐겨찾기 상태로 페이징된 책들을 불러올 수 있다`() = runBlocking {
        dao.insertAll(dummyBooks)

        val pagingSource = dao.selectByBookmarked(true)
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false,
            ),
        ) as PagingSource.LoadResult.Page

        assertTrue(loadResult.data.all { it.isBookmarked })
    }

    @Test
    fun `모든 책들을 페이징된 리스트로 불러올 수 있다`() = runBlocking {
        dao.insertAll(dummyBooks)

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
        val book = dummyBooks[0].copy(isBookmarked = false)
        dao.insert(book)

        dao.updateBookmarkStatus(book.isbn, true)

        val updatedBook = dao.selectByIsbn(book.isbn)
        assertEquals(true, updatedBook.isBookmarked)
    }

    @Test
    fun `책이 존재하는지 확인할 수 있다`() = runBlocking {
        val book = dummyBooks[0]
        dao.insert(book)

        val exists = dao.isBookExistsByIsbn(book.isbn)
        val notExists = dao.isBookExistsByIsbn("999")

        assertTrue(exists)
        assertFalse(notExists)
    }

    @Test
    fun `책을 삭제할 수 있다`() = runBlocking {
        val book = dummyBooks[0]
        dao.insert(book)

        dao.deleteByIsbn(book.isbn)

        val result = dao.selectByIsbn(book.isbn)
        assertNull(result)
    }

    @Test
    fun `모든 책을 삭제할 수 있다`() = runBlocking {
        dao.insertAll(dummyBooks)

        dao.deleteAll()

        val result1 = dao.selectByIsbn(dummyBooks[0].isbn)
        val result2 = dao.selectByIsbn(dummyBooks[1].isbn)

        assertNull(result1)
        assertNull(result2)
    }
}
