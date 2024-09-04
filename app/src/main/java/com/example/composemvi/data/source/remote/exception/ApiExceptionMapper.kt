package com.example.composemvi.data.source.remote.exception

import com.example.composemvi.data.source.remote.exception.ApiErrorMessage.JSON_PARSE_ERROR_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiErrorMessage.NETWORK_ERROR_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiErrorMessage.TIMEOUT_ERROR_MESSAGE
import com.example.composemvi.data.source.remote.exception.ApiErrorMessage.UNKNOWN_API_ERROR_MESSAGE

object ApiExceptionMapper {

    fun toException(exception: Throwable): RemoteApiException {
        return when (exception) {
            is java.net.UnknownHostException ->
                RemoteApiException.NetworkException(
                    exception.message ?: NETWORK_ERROR_MESSAGE,
                )

            is retrofit2.HttpException -> toHttpException(exception)
            is java.net.SocketTimeoutException ->
                RemoteApiException.TimeoutException(
                    exception.message ?: TIMEOUT_ERROR_MESSAGE,
                )

            is kotlinx.serialization.SerializationException ->
                RemoteApiException.JsonParseException(
                    exception.message ?: JSON_PARSE_ERROR_MESSAGE,
                )

            else ->
                RemoteApiException.UnknownApiException(
                    exception.message ?: UNKNOWN_API_ERROR_MESSAGE,
                )
        }
    }

    private fun toHttpException(exception: retrofit2.HttpException): RemoteApiException {
        val errorBody = exception.response()?.errorBody()?.string().orEmpty()
        val code = exception.code()

        val errorMessage = errorBody.ifEmpty { ApiErrorMessage.getDefaultErrorMessageForCode(code) }

        return when (code) {
            400 -> RemoteApiException.ClientException(errorMessage)
            401 -> RemoteApiException.UnauthorizedException(errorMessage)
            403 -> RemoteApiException.ForbiddenException(errorMessage)
            404 -> RemoteApiException.NotFoundException(errorMessage)
            429 -> RemoteApiException.TooManyRequestsException(errorMessage)
            in 400..499 -> RemoteApiException.ClientException(errorMessage)
            500 -> RemoteApiException.InternalServerException(errorMessage)
            in 500..599 -> RemoteApiException.ServerException(errorMessage)
            else -> RemoteApiException.UnknownHttpException(errorMessage)
        }
    }
}
