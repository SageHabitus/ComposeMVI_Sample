package com.example.composemvi.domain.exception

sealed class DomainException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    data class DatabaseException(override val message: String) : DomainException(message)
    data class NetworkException(override val message: String) : DomainException(message)
    data class UnknownException(override val message: String) : DomainException(message)
}
