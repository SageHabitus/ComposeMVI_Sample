package com.example.composemvi.data.source.local.exception

sealed class LocalDatabaseException : Throwable() {

    // SQLite Exceptions
    data class ConstraintException(
        override val message: String,
    ) : LocalDatabaseException()

    data class DatabaseCorruptException(
        override val message: String,
    ) : LocalDatabaseException()

    data class FullException(
        override val message: String,
    ) : LocalDatabaseException()

    data class AccessPermException(
        override val message: String,
    ) : LocalDatabaseException()

    data class DiskIOException(
        override val message: String,
    ) : LocalDatabaseException()

    data class AbortException(
        override val message: String,
    ) : LocalDatabaseException()

    data class DatatypeMismatchException(
        override val message: String,
    ) : LocalDatabaseException()

    data class GenericSQLiteException(
        override val message: String,
    ) : LocalDatabaseException()

    // General SQL Exceptions
    data class SqlException(
        override val message: String,
    ) : LocalDatabaseException()

    data class OperationCanceledException(
        override val message: String,
    ) : LocalDatabaseException()

    data class CursorIndexOutOfBoundsException(
        override val message: String,
    ) : LocalDatabaseException()

    data class StaleDataException(
        override val message: String,
    ) : LocalDatabaseException()

    // Illegal State and Argument Exceptions
    data class IllegalStateException(
        override val message: String,
    ) : LocalDatabaseException()

    data class IllegalArgumentException(
        override val message: String,
    ) : LocalDatabaseException()

    // Coroutines Exceptions
    data class CancellationException(
        override val message: String,
    ) : LocalDatabaseException()

    data class TimeoutException(
        override val message: String,
    ) : LocalDatabaseException()

    // Paging Exceptions
    data class PagingIOException(
        override val message: String,
    ) : LocalDatabaseException()

    data class PagingInvalidException(
        override val message: String,
    ) : LocalDatabaseException()

    // Unknown or Unhandled Exceptions
    data class UnknownDatabaseException(
        override val message: String,
    ) : LocalDatabaseException()
}
