package com.example.composemvi.domain.exception

import com.example.composemvi.data.exception.DataException
import com.example.composemvi.domain.exception.DomainExceptionMessage.DATABASE_ERROR_MESSAGE
import com.example.composemvi.domain.exception.DomainExceptionMessage.NETWORK_ERROR_MESSAGE
import com.example.composemvi.domain.exception.DomainExceptionMessage.UNKNOWN_ERROR_MESSAGE

object DomainExceptionMapper {

    fun toDomainException(exception: Throwable): DomainException {
        return when (exception) {
            is DataException.LocalDataSourceException -> {
                DomainException.DatabaseException(
                    String.format(
                        DATABASE_ERROR_MESSAGE,
                        exception.localException.message,
                    ),
                )
            }

            is DataException.RemoteDataSourceException -> {
                DomainException.NetworkException(
                    String.format(
                        NETWORK_ERROR_MESSAGE,
                        exception.remoteException.message,
                    ),
                )
            }

            else -> DomainException.UnknownException(
                exception.message ?: UNKNOWN_ERROR_MESSAGE,
            )
        }
    }
}
