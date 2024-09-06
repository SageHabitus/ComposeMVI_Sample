package com.example.composemvi.data.exception

import com.example.composemvi.data.source.local.exception.RoomDatabaseException
import com.example.composemvi.data.source.remote.exception.RemoteApiException

sealed class DataException : Exception() {

    data class LocalDataSourceException(
        val localException: RoomDatabaseException,
    ) : DataException()

    data class RemoteDataSourceException(
        val remoteException: RemoteApiException,
    ) : DataException()

    data class UnknownDataSourceException(
        override val message: String,
    ) : DataException()
}
