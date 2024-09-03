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
import com.example.composemvi.data.source.local.exception.DatabaseExceptionMapper
import com.example.composemvi.data.source.local.exception.LocalDatabaseException
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.util.concurrent.TimeoutException

@RunWith(JUnit4::class)
class DatabaseExceptionMapperTest {

    @Test
    fun `SQLiteConstraintException is mapped to ConstraintException`() {
        val exception = SQLiteConstraintException("Constraint violation")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.ConstraintException)
    }

    @Test
    fun `SQLiteDatabaseCorruptException is mapped to DatabaseCorruptException`() {
        val exception = SQLiteDatabaseCorruptException("Database is corrupt")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.DatabaseCorruptException)
    }

    @Test
    fun `SQLiteFullException is mapped to FullException`() {
        val exception = SQLiteFullException("Database is full")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.FullException)
    }

    @Test
    fun `SQLiteAccessPermException is mapped to AccessPermException`() {
        val exception = SQLiteAccessPermException("Access permission denied")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.AccessPermException)
    }

    @Test
    fun `SQLiteDiskIOException is mapped to DiskIOException`() {
        val exception = SQLiteDiskIOException("Disk I/O error")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.DiskIOException)
    }

    @Test
    fun `SQLiteAbortException is mapped to AbortException`() {
        val exception = SQLiteAbortException("Operation aborted")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.AbortException)
    }

    @Test
    fun `SQLiteDatatypeMismatchException is mapped to DatatypeMismatchException`() {
        val exception = SQLiteDatatypeMismatchException("Data type mismatch")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.DatatypeMismatchException)
    }

    @Test
    fun `Generic SQLiteException is mapped to GenericSQLiteException`() {
        val exception = SQLiteException("SQLite error")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.GenericSQLiteException)
    }

    @Test
    fun `SQLException is mapped to SqlException`() {
        val exception = SQLException("SQL error")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.SqlException)
    }

    @Test
    fun `OperationCanceledException is mapped to OperationCanceledException`() {
        val exception = android.os.OperationCanceledException("Operation canceled")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.OperationCanceledException)
    }

    @Test
    fun `CursorIndexOutOfBoundsException is mapped to CursorIndexOutOfBoundsException`() {
        val exception = CursorIndexOutOfBoundsException("Cursor index out of bounds")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.CursorIndexOutOfBoundsException)
    }

    @Test
    fun `StaleDataException is mapped to StaleDataException`() {
        val exception = StaleDataException("Stale data error")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.StaleDataException)
    }

    @Test
    fun `IllegalStateException is mapped to IllegalStateException`() {
        val exception = IllegalStateException("Illegal state")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.IllegalStateException)
    }

    @Test
    fun `IllegalArgumentException is mapped to IllegalArgumentException`() {
        val exception = IllegalArgumentException("Illegal argument")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.IllegalArgumentException)
    }

    @Test
    fun `TimeoutException is mapped to TimeoutException`() {
        val exception = TimeoutException("Operation timed out")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.TimeoutException)
    }

    @Test
    fun `IOException is mapped to PagingIOException`() {
        val exception = IOException("Paging I/O error")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.PagingIOException)
    }

    @Test
    fun `Unknown exception is mapped to UnknownDatabaseException`() {
        val exception = Exception("Unknown error")
        val mappedException = DatabaseExceptionMapper.toException(exception)
        assertTrue(mappedException is LocalDatabaseException.UnknownDatabaseException)
    }
}
