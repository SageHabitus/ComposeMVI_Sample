package com.example.composemvi.data.exception

import com.example.composemvi.data.exception.DataExceptionMessage.UNKNOWN_ERROR
import com.example.composemvi.data.source.local.exception.RoomDatabaseException
import com.example.composemvi.data.source.remote.exception.RemoteApiException

object DataExceptionMapper {

    fun toDataException(exception: Throwable): DataException {
        return when (exception) {
            is RoomDatabaseException -> DataException.LocalDataSourceException(exception)
            is RemoteApiException -> DataException.RemoteDataSourceException(exception)
            else -> DataException.UnknownDataSourceException(
                exception.message ?: UNKNOWN_ERROR,
            )
        }
    }
}
