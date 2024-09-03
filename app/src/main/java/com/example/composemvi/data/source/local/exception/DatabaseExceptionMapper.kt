package com.example.composemvi.data.source.local.exception

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
import java.io.IOException
import java.util.concurrent.TimeoutException

object DatabaseExceptionMapper {

    fun toException(exception: Throwable): LocalDatabaseException {
        return when (exception) {
            // SQLite Exceptions
            is SQLiteConstraintException -> LocalDatabaseException.ConstraintException(
                exception.message ?: "Constraint violation",
            )

            is SQLiteDatabaseCorruptException -> LocalDatabaseException.DatabaseCorruptException(
                exception.message ?: "Database is corrupt",
            )

            is SQLiteFullException -> LocalDatabaseException.FullException(
                exception.message ?: "Database is full",
            )

            is SQLiteAccessPermException -> LocalDatabaseException.AccessPermException(
                exception.message ?: "Access permission denied",
            )

            is SQLiteDiskIOException -> LocalDatabaseException.DiskIOException(
                exception.message ?: "Disk I/O error",
            )

            is SQLiteAbortException -> LocalDatabaseException.AbortException(
                exception.message ?: "Operation aborted",
            )

            is SQLiteDatatypeMismatchException -> LocalDatabaseException.DatatypeMismatchException(
                exception.message ?: "Data type mismatch",
            )

            is SQLiteException -> LocalDatabaseException.GenericSQLiteException(
                exception.message ?: "SQLite error",
            )

            // General SQL Exceptions
            is SQLException -> LocalDatabaseException.SqlException(
                exception.message ?: "SQL error",
            )

            is android.os.OperationCanceledException -> LocalDatabaseException.OperationCanceledException(
                exception.message ?: "Operation canceled",
            )

            is CursorIndexOutOfBoundsException -> LocalDatabaseException.CursorIndexOutOfBoundsException(
                exception.message ?: "Cursor index out of bounds",
            )

            is StaleDataException -> LocalDatabaseException.StaleDataException(
                exception.message ?: "Stale data error",
            )

            // Illegal State and Argument Exceptions
            is IllegalStateException -> LocalDatabaseException.IllegalStateException(
                exception.message ?: "Illegal state",
            )

            is IllegalArgumentException -> LocalDatabaseException.IllegalArgumentException(
                exception.message ?: "Illegal argument",
            )

            is TimeoutException -> LocalDatabaseException.TimeoutException(
                exception.message ?: "Operation timed out",
            )

            // Paging Exceptions
            is IOException -> LocalDatabaseException.PagingIOException(
                exception.message ?: "Paging I/O error",
            )

            // Unknown Exceptions
            else -> LocalDatabaseException.UnknownDatabaseException(
                exception.message ?: "Unknown database error",
            )
        }
    }
}
