package com.example.composemvi.data.source.remote.exception

sealed class RemoteApiException : Throwable() {

    data class NetworkException(
        override val message: String,
    ) : RemoteApiException()

    data class ClientException(
        override val message: String,
    ) : RemoteApiException()

    data class ServerException(
        override val message: String,
    ) : RemoteApiException()

    data class TimeoutException(
        override val message: String,
    ) : RemoteApiException()

    data class JsonParseException(
        override val message: String,
    ) : RemoteApiException()

    data class UnauthorizedException(
        override val message: String,
    ) : RemoteApiException()

    data class ForbiddenException(
        override val message: String,
    ) : RemoteApiException()

    data class NotFoundException(
        override val message: String,
    ) : RemoteApiException()

    data class TooManyRequestsException(
        override val message: String,
    ) : RemoteApiException()

    data class InternalServerException(
        override val message: String,
    ) : RemoteApiException()

    data class UnknownHttpException(
        override val message: String,
    ) : RemoteApiException()

    data class UnknownApiException(
        override val message: String,
    ) : RemoteApiException()
}
