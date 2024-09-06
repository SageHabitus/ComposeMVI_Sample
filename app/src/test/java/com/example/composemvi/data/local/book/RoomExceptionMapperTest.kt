package com.example.composemvi.data.local.book

import android.database.CursorIndexOutOfBoundsException
import android.database.SQLException
import android.database.StaleDataException
import android.database.sqlite.SQLiteAbortException
import android.database.sqlite.SQLiteAccessPermException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDatatypeMismatchException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import com.example.composemvi.data.source.local.exception.RoomExceptionMapper
import com.example.composemvi.data.source.local.exception.RoomDatabaseException
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.util.concurrent.TimeoutException

@RunWith(JUnit4::class)
class RoomExceptionMapperTest {

    @Test
    fun `SQLiteConstraintException is mapped to ConstraintException`() {
        val exception = SQLiteConstraintException("Constraint violation")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.ConstraintException)
    }

    @Test
    fun `SQLiteDatabaseCorruptException is mapped to DatabaseCorruptException`() {
        val exception = SQLiteDatabaseCorruptException("Database is corrupt")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.DatabaseCorruptException)
    }

    @Test
    fun `SQLiteFullException is mapped to FullException`() {
        val exception = SQLiteFullException("Database is full")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.FullException)
    }

    @Test
    fun `SQLiteAccessPermException is mapped to AccessPermException`() {
        val exception = SQLiteAccessPermException("Access permission denied")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.AccessPermException)
    }

    @Test
    fun `SQLiteDiskIOException is mapped to DiskIOException`() {
        val exception = SQLiteDiskIOException("Disk I/O error")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.DiskIOException)
    }

    @Test
    fun `SQLiteAbortException is mapped to AbortException`() {
        val exception = SQLiteAbortException("Operation aborted")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.AbortException)
    }

    @Test
    fun `SQLiteDatatypeMismatchException is mapped to DatatypeMismatchException`() {
        val exception = SQLiteDatatypeMismatchException("Data type mismatch")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.DatatypeMismatchException)
    }

    @Test
    fun `Generic SQLiteException is mapped to GenericSQLiteException`() {
        val exception = SQLiteException("SQLite error")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.GenericSQLiteException)
    }

    @Test
    fun `SQLException is mapped to SqlException`() {
        val exception = SQLException("SQL error")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.SqlException)
    }

    @Test
    fun `OperationCanceledException is mapped to OperationCanceledException`() {
        val exception = android.os.OperationCanceledException("Operation canceled")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.OperationCanceledException)
    }

    @Test
    fun `CursorIndexOutOfBoundsException is mapped to CursorIndexOutOfBoundsException`() {
        val exception = CursorIndexOutOfBoundsException("Cursor index out of bounds")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.CursorIndexOutOfBoundsException)
    }

    @Test
    fun `StaleDataException is mapped to StaleDataException`() {
        val exception = StaleDataException("Stale data error")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.StaleDataException)
    }

    @Test
    fun `IllegalStateException is mapped to IllegalStateException`() {
        val exception = IllegalStateException("Illegal state")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.IllegalStateException)
    }

    @Test
    fun `IllegalArgumentException is mapped to IllegalArgumentException`() {
        val exception = IllegalArgumentException("Illegal argument")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.IllegalArgumentException)
    }

    @Test
    fun `TimeoutException is mapped to TimeoutException`() {
        val exception = TimeoutException("Operation timed out")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.TimeoutException)
    }

    @Test
    fun `IOException is mapped to PagingIOException`() {
        val exception = IOException("Paging I/O error")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.PagingIOException)
    }

    @Test
    fun `Unknown exception is mapped to UnknownDatabaseException`() {
        val exception = Exception("Unknown error")
        val mappedException = RoomExceptionMapper.toException(exception)
        assertTrue(mappedException is RoomDatabaseException.UnknownDatabaseException)
    }
}
