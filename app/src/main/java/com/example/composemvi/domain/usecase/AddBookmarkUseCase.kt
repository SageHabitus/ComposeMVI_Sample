package com.example.composemvi.domain.usecase

import com.example.composemvi.data.repository.BookRepository
import com.example.composemvi.domain.exception.DomainExceptionMapper
import javax.inject.Inject

class AddBookmarkUseCase @Inject constructor(
    private val bookRepo: BookRepository,
) {
    suspend fun execute(isbn: String) {
        return runCatching {
            bookRepo.updateBookmarkStatus(isbn = isbn, isBookmarked = true)
        }.getOrElse { exception ->
            throw DomainExceptionMapper.toDomainException(exception)
        }
    }
}
