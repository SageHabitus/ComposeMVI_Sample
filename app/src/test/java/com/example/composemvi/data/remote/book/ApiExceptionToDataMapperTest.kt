package com.example.composemvi.data.remote.book

import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.FORBIDDEN_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.INTERNAL_SERVER_ERROR_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.NETWORK_ERROR_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.NOT_FOUND_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.TIMEOUT_ERROR_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMessage.UNAUTHORIZED_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiExceptionMapper
import com.example.composemvi.data.source.remote.exception.RemoteApiException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApiExceptionToDataMapperTest {

    @Test
    fun `네트워크 예러 발생 시 NetworkException을 반환해야 한다`() {
        val exception = UnknownHostException(NETWORK_ERROR_MESSAGE)
        val result = ApiExceptionMapper.toException(exception)

        assertTrue(result is RemoteApiException.NetworkException)
        assertEquals(NETWORK_ERROR_MESSAGE, result.message)
    }

    @Test
    fun `타임아웃 예러 발생 시 TimeoutException을 반환해야 한다`() {
        val exception = SocketTimeoutException(TIMEOUT_ERROR_MESSAGE)
        val result = ApiExceptionMapper.toException(exception)

        assertTrue(result is RemoteApiException.TimeoutException)
        assertEquals(TIMEOUT_ERROR_MESSAGE, result.message)
    }

    @Test
    fun `클라이언트 401 에러 발생 시 UnauthorizedException을 반환해야 한다`() {
        val exception = HttpException(Response.error<String>(401, UNAUTHORIZED_MESSAGE.toResponseBody(null)))
        val result = ApiExceptionMapper.toException(exception)

        assertTrue(result is RemoteApiException.UnauthorizedException)
        assertEquals(UNAUTHORIZED_MESSAGE, result.message)
    }

    @Test
    fun `클라이언트 403 에러 발생 시 ForbiddenException을 반환해야 한다`() {
        val exception = HttpException(Response.error<String>(403, FORBIDDEN_MESSAGE.toResponseBody(null)))
        val result = ApiExceptionMapper.toException(exception)

        assertTrue(result is RemoteApiException.ForbiddenException)
        assertEquals(FORBIDDEN_MESSAGE, result.message)
    }

    @Test
    fun `404 에러 발생 시 NotFoundException을 반환해야 한다`() {
        val exception = HttpException(Response.error<String>(404, NOT_FOUND_MESSAGE.toResponseBody(null)))
        val result = ApiExceptionMapper.toException(exception)

        assertTrue(result is RemoteApiException.NotFoundException)
        assertEquals(NOT_FOUND_MESSAGE, result.message)
    }

    @Test
    fun `500 에러 발생 시 InternalServerException을 반환해야 한다`() {
        val exception = HttpException(Response.error<String>(500, INTERNAL_SERVER_ERROR_MESSAGE.toResponseBody(null)))
        val result = ApiExceptionMapper.toException(exception)

        assertTrue(result is RemoteApiException.InternalServerException)
        assertEquals(INTERNAL_SERVER_ERROR_MESSAGE, result.message)
    }
}
