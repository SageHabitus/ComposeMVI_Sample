package com.example.composemvi.data.book

import com.example.composemvi.data.exception.DataException
import com.example.composemvi.data.exception.DataExceptionMapper
import com.example.composemvi.data.exception.DataExceptionMessage
import com.example.composemvi.data.source.local.exception.RoomDatabaseException
import com.example.composemvi.data.source.remote.exception.RemoteApiException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.net.SocketTimeoutException

class DataExceptionMapperTest {

    @Test
    fun `RoomDatabaseException을 LocalDataSourceException으로 매핑해야 한다`() {
        val roomException = RoomDatabaseException.GenericSQLiteException(DataExceptionMessage.DATABASE_ERROR)
        val result = DataExceptionMapper.toDataException(roomException)

        assertTrue(result is DataException.LocalDataSourceException)
        assertEquals(DataExceptionMessage.DATABASE_ERROR, roomException.message)
    }

    @Test
    fun `RemoteApiException을 RemoteDataSourceException으로 매핑해야 한다`() {
        val remoteException = RemoteApiException.NetworkException(DataExceptionMessage.NETWORK_ERROR)
        val result = DataExceptionMapper.toDataException(remoteException)

        assertTrue(result is DataException.RemoteDataSourceException)
        assertEquals(DataExceptionMessage.NETWORK_ERROR, remoteException.message)
    }

    @Test
    fun `SocketTimeoutException을 UnknownDataSourceException으로 매핑해야 한다`() {
        val timeoutException = SocketTimeoutException(DataExceptionMessage.TIMEOUT_ERROR)
        val result = DataExceptionMapper.toDataException(timeoutException)

        assertTrue(result is DataException.UnknownDataSourceException)
        assertEquals(DataExceptionMessage.TIMEOUT_ERROR, result.message)
    }

    @Test
    fun `알 수 없는 예외를 UnknownDataSourceException으로 매핑해야 한다`() {
        val unknownException = RuntimeException(DataExceptionMessage.UNKNOWN_ERROR)
        val result = DataExceptionMapper.toDataException(unknownException)

        assertTrue(result is DataException.UnknownDataSourceException)
        assertEquals(DataExceptionMessage.UNKNOWN_ERROR, result.message)
    }
}
