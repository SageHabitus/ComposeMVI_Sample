package com.example.composemvi.data.remote.book

import com.example.composemvi.data.book.dummy.book.DummyBooks.getMockBookResponse
import com.example.composemvi.data.source.remote.api.BookApi
import com.example.composemvi.data.source.remote.exception.RemoteApiException
import com.example.composemvi.data.source.remote.model.BookResponseModel
import com.example.composemvi.data.source.remote.source.BookRemoteDataSource
import com.example.composemvi.data.source.remote.source.BookRemoteDataSourceImpl
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BookRemoteDataSourceImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var bookRemoteDataSource: BookRemoteDataSource
    private lateinit var bookApi: BookApi
    private lateinit var bookMockResponse: MockResponse

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        bookMockResponse = getMockBookResponse()
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(3, java.util.concurrent.TimeUnit.SECONDS) // 3초 타임아웃
            .build()

        bookApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(BookApi::class.java)

        bookRemoteDataSource = BookRemoteDataSourceImpl(bookApi)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `책 검색 API 정상 호출 시 결과를 반환해야 한다`() = runBlocking {
        mockWebServer.enqueue(bookMockResponse)

        val result = bookRemoteDataSource.searchBooks("query", 1, 10)

        val parsedData = Json.decodeFromString<BookResponseModel>(bookMockResponse.getBody()!!.readUtf8())

        val firstBookTitle = parsedData.documents[0].title
        val secondBookTitle = parsedData.documents[1].title

        assertEquals(80, result.documents.size)
        assertEquals(firstBookTitle, result.documents[0].title)
        assertEquals(secondBookTitle, result.documents[1].title)
    }

    @Test
    fun `404 에러 발생 시 NotFoundException 반환해야 한다`() = runBlocking {
        mockWebServer.enqueue(bookMockResponse.setResponseCode(404))

        val exception = runCatching {
            bookRemoteDataSource.searchBooks("query", 1, 10)
        }.exceptionOrNull()

        assertTrue(exception is RemoteApiException.NotFoundException)
    }

    @Test
    fun `401 에러 발생 시 UnauthorizedException 반환해야 한다`() = runBlocking {
        mockWebServer.enqueue(bookMockResponse.setResponseCode(401))

        val exception = runCatching {
            bookRemoteDataSource.searchBooks("query", 1, 10)
        }.exceptionOrNull()

        assertTrue(exception is RemoteApiException.UnauthorizedException)
    }

    @Test
    fun `500 에러 발생 시 InternalServerException 반환해야 한다`() = runBlocking {
        mockWebServer.enqueue(bookMockResponse.setResponseCode(500))

        val exception = runCatching {
            bookRemoteDataSource.searchBooks("query", 1, 10)
        }.exceptionOrNull()

        assertTrue(exception is RemoteApiException.InternalServerException)
    }

    @Test
    fun `타임아웃 에러 발생 시 TimeoutException 반환해야 한다`() = runBlocking {
        val mockResponse = bookMockResponse
            .setBodyDelay(5, TimeUnit.SECONDS)

        mockWebServer.enqueue(mockResponse)

        val exception = runCatching {
            bookRemoteDataSource.searchBooks("query", 1, 10)
        }.exceptionOrNull()

        assertTrue(exception is RemoteApiException.TimeoutException)
    }

    @Test
    fun `너무 많은 요청으로 429 에러 발생 시 TooManyRequestsException 반환해야 한다`() = runBlocking {
        mockWebServer.enqueue(bookMockResponse.setResponseCode(429))

        val exception = runCatching {
            bookRemoteDataSource.searchBooks("query", 1, 10)
        }.exceptionOrNull()

        assertTrue(exception is RemoteApiException.TooManyRequestsException)
    }
}
