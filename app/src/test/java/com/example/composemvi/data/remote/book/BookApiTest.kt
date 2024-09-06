package com.example.composemvi.data.remote.book

import com.example.composemvi.data.book.dummy.TestResourceLoader.BOOK_REMOTE_TEST_JSON
import com.example.composemvi.data.book.dummy.TestResourceLoader.getJsonStringFromResource
import com.example.composemvi.data.source.remote.api.BookApi
import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.INTERNAL_SERVER_ERROR_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.TIMEOUT_ERROR_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.TOO_MANY_REQUESTS_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.UNAUTHORIZED_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMapper
import com.example.composemvi.data.source.remote.exception.RemoteApiException
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.nio.charset.StandardCharsets
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BookApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var bookApi: BookApi
    private lateinit var dummyBooks: String

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        dummyBooks = getJsonStringFromResource(BOOK_REMOTE_TEST_JSON)
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
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    private fun loadJsonFromResource(fileName: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        val source = inputStream?.source()?.buffer()
        return source?.readString(StandardCharsets.UTF_8) ?: ""
    }

    @Test
    fun `책 검색 API 호출 시 정상 응답을 처리해야 한다`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(dummyBooks)

        mockWebServer.enqueue(mockResponse)

        val result = bookApi.searchBooks("query", 1, 10)

        assertEquals(80, result.documents.size)
        val parsedJson = Json.parseToJsonElement(dummyBooks).jsonObject
        val firstBookTitle =
            parsedJson["documents"]?.jsonArray?.get(0)?.jsonObject?.get("title")?.jsonPrimitive?.content
        val secondBookTitle =
            parsedJson["documents"]?.jsonArray?.get(1)?.jsonObject?.get("title")?.jsonPrimitive?.content

        assertEquals(firstBookTitle, result.documents[0].title)
        assertEquals(secondBookTitle, result.documents[1].title)
    }

    @Test
    fun `클라이언트 401 에러 발생 시 UnauthorizedException을 반환해야 한다`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(401)
            .setBody(UNAUTHORIZED_MESSAGE)

        mockWebServer.enqueue(mockResponse)

        val exception = runCatching {
            bookApi.searchBooks("query", 1, 10)
        }.exceptionOrNull()

        assertTrue(exception is HttpException)
        val mappedException = ApiExceptionMapper.toException(exception)
        assertTrue(mappedException is RemoteApiException.UnauthorizedException)
        assertEquals(UNAUTHORIZED_MESSAGE, mappedException.message)
    }

    @Test
    fun `500 에러 발생 시 InternalServerException을 반환해야 한다`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody(INTERNAL_SERVER_ERROR_MESSAGE)

        mockWebServer.enqueue(mockResponse)

        val exception = runCatching {
            bookApi.searchBooks("query", 1, 10)
        }.exceptionOrNull()

        assertTrue(exception is HttpException)
        val mappedException = ApiExceptionMapper.toException(exception)
        assertTrue(mappedException is RemoteApiException.InternalServerException)
        assertEquals(INTERNAL_SERVER_ERROR_MESSAGE, mappedException.message)
    }

    @Test
    fun `타임아웃 에러 발생 시 TimeoutException을 반환해야 한다`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(dummyBooks)
            .setBodyDelay(5, java.util.concurrent.TimeUnit.SECONDS)

        mockWebServer.enqueue(mockResponse)

        val exception = runCatching {
            bookApi.searchBooks("query", 1, 10)
        }.exceptionOrNull()

        println(exception)

        assertTrue(exception is java.net.SocketTimeoutException)
        val mappedException = ApiExceptionMapper.toException(exception)
        assertTrue(mappedException is RemoteApiException.TimeoutException)

        val defaultMessage = mappedException.message.takeIf { it.isNotEmpty() } ?: TIMEOUT_ERROR_MESSAGE
        assertEquals(defaultMessage, mappedException.message)
    }

    @Test
    fun `많은 요청을 보내는 경우 429 에러가 발생하면 TooManyRequestsException을 반환해야 한다`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(429)
            .setBody(TOO_MANY_REQUESTS_MESSAGE)

        mockWebServer.enqueue(mockResponse)

        val exception = runCatching {
            bookApi.searchBooks("query", 1, 10)
        }.exceptionOrNull()

        assertTrue(exception is HttpException)
        val mappedException = ApiExceptionMapper.toException(exception)
        assertTrue(mappedException is RemoteApiException.TooManyRequestsException)
        assertEquals(TOO_MANY_REQUESTS_MESSAGE, mappedException.message)
    }
}
