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
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.ACCESS_PERMISSION_DENIED_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.CONSTRAINT_VIOLATION_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.CURSOR_INDEX_OUT_OF_BOUNDS_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.DATABASE_CORRUPT_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.DATABASE_FULL_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.DATA_TYPE_MISMATCH_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.DISK_IO_ERROR_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.ILLEGAL_ARGUMENT_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.ILLEGAL_STATE_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.OPERATION_ABORTED_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.OPERATION_CANCELED_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.PAGING_IO_ERROR_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.SQLITE_ERROR_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.SQL_ERROR_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.STALE_DATA_ERROR_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.TIMEOUT_ERROR_MESSAGE
import com.example.composemvi.data.source.local.exception.RoomExceptionMessage.UNKNOWN_DATABASE_ERROR_MESSAGE
import java.io.IOException
import java.util.concurrent.TimeoutException

object RoomExceptionMapper {

    fun toException(exception: Throwable): RoomDatabaseException {
        return when (exception) {
            is SQLiteConstraintException ->
                RoomDatabaseException.ConstraintException(
                    exception.message ?: CONSTRAINT_VIOLATION_MESSAGE,
                )

            is SQLiteDatabaseCorruptException ->
                RoomDatabaseException.DatabaseCorruptException(
                    exception.message ?: DATABASE_CORRUPT_MESSAGE,
                )

            is SQLiteFullException ->
                RoomDatabaseException.FullException(
                    exception.message ?: DATABASE_FULL_MESSAGE,
                )

            is SQLiteAccessPermException ->
                RoomDatabaseException.AccessPermException(
                    exception.message ?: ACCESS_PERMISSION_DENIED_MESSAGE,
                )

            is SQLiteDiskIOException ->
                RoomDatabaseException.DiskIOException(
                    exception.message ?: DISK_IO_ERROR_MESSAGE,
                )

            is SQLiteAbortException ->
                RoomDatabaseException.AbortException(
                    exception.message ?: OPERATION_ABORTED_MESSAGE,
                )

            is SQLiteDatatypeMismatchException ->
                RoomDatabaseException.DatatypeMismatchException(
                    exception.message ?: DATA_TYPE_MISMATCH_MESSAGE,
                )

            is SQLiteException ->
                RoomDatabaseException.GenericSQLiteException(
                    exception.message ?: SQLITE_ERROR_MESSAGE,
                )

            is SQLException ->
                RoomDatabaseException.SqlException(
                    exception.message ?: SQL_ERROR_MESSAGE,
                )

            is android.os.OperationCanceledException ->
                RoomDatabaseException.OperationCanceledException(
                    exception.message ?: OPERATION_CANCELED_MESSAGE,
                )

            is CursorIndexOutOfBoundsException ->
                RoomDatabaseException.CursorIndexOutOfBoundsException(
                    exception.message ?: CURSOR_INDEX_OUT_OF_BOUNDS_MESSAGE,
                )

            is StaleDataException ->
                RoomDatabaseException.StaleDataException(
                    exception.message ?: STALE_DATA_ERROR_MESSAGE,
                )

            is IllegalStateException ->
                RoomDatabaseException.IllegalStateException(
                    exception.message ?: ILLEGAL_STATE_MESSAGE,
                )

            is IllegalArgumentException ->
                RoomDatabaseException.IllegalArgumentException(
                    exception.message ?: ILLEGAL_ARGUMENT_MESSAGE,
                )

            is TimeoutException ->
                RoomDatabaseException.TimeoutException(
                    exception.message ?: TIMEOUT_ERROR_MESSAGE,
                )

            is IOException ->
                RoomDatabaseException.PagingIOException(
                    exception.message ?: PAGING_IO_ERROR_MESSAGE,
                )

            else -> RoomDatabaseException.UnknownDatabaseException(
                exception.message ?: UNKNOWN_DATABASE_ERROR_MESSAGE,
            )
        }
    }
}
