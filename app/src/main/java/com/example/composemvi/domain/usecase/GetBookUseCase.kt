package com.example.composemvi.domain.usecase

import com.example.composemvi.data.repository.BookRepository
import com.example.composemvi.domain.mapper.toDomainModel
import com.example.composemvi.domain.model.BookDomainModel
import javax.inject.Inject

class GetBookUseCase
@Inject
constructor(
    private val bookRepo: BookRepository,
) {
    suspend fun execute(isbn: String): BookDomainModel {
        return bookRepo
            .getBookByIsbn(isbn)
            .toDomainModel()
    }
}
