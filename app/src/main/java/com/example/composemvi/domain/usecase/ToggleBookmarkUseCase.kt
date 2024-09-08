package com.example.composemvi.domain.usecase

import com.example.composemvi.domain.exception.DomainExceptionMapper
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
) {
    private val mutex = Mutex()

    suspend fun execute(isbn: String, isBookmarked: Boolean) = mutex.withLock {
        runCatching {
            if (isBookmarked) {
                removeBookmarkUseCase.execute(isbn = isbn)
            } else {
                addBookmarkUseCase.execute(isbn = isbn)
            }
        }.getOrElse { exception ->
            throw DomainExceptionMapper.toDomainException(exception)
        }
    }
}
