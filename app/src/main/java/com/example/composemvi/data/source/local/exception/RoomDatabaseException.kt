package com.example.composemvi.data.source.local.exception

sealed class RoomDatabaseException : Throwable() {

    // SQLite Exceptions
    data class ConstraintException(
        override val message: String,
    ) : RoomDatabaseException()

    data class DatabaseCorruptException(
        override val message: String,
    ) : RoomDatabaseException()

    data class FullException(
        override val message: String,
    ) : RoomDatabaseException()

    data class AccessPermException(
        override val message: String,
    ) : RoomDatabaseException()

    data class DiskIOException(
        override val message: String,
    ) : RoomDatabaseException()

    data class AbortException(
        override val message: String,
    ) : RoomDatabaseException()

    data class DatatypeMismatchException(
        override val message: String,
    ) : RoomDatabaseException()

    data class GenericSQLiteException(
        override val message: String,
    ) : RoomDatabaseException()

    // General SQL Exceptions
    data class SqlException(
        override val message: String,
    ) : RoomDatabaseException()

    data class OperationCanceledException(
        override val message: String,
    ) : RoomDatabaseException()

    data class CursorIndexOutOfBoundsException(
        override val message: String,
    ) : RoomDatabaseException()

    data class StaleDataException(
        override val message: String,
    ) : RoomDatabaseException()

    // Illegal State and Argument Exceptions
    data class IllegalStateException(
        override val message: String,
    ) : RoomDatabaseException()

    data class IllegalArgumentException(
        override val message: String,
    ) : RoomDatabaseException()

    data class TimeoutException(
        override val message: String,
    ) : RoomDatabaseException()

    // Paging Exceptions
    data class PagingIOException(
        override val message: String,
    ) : RoomDatabaseException()

    data class PagingInvalidException(
        override val message: String,
    ) : RoomDatabaseException()

    // Unknown or Unhandled Exceptions
    data class UnknownDatabaseException(
        override val message: String,
    ) : RoomDatabaseException()
}
