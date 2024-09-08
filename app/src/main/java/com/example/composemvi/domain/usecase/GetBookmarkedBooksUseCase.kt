package com.example.composemvi.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.composemvi.data.repository.BookRepository
import com.example.composemvi.domain.mapper.toDomainModel
import com.example.composemvi.domain.model.BookDomainModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBookmarkedBooksUseCase
@Inject
constructor(
    private val bookRepo: BookRepository,
) {
    suspend fun execute(): Flow<PagingData<BookDomainModel>> {
        return bookRepo.getAllBookmarkedBooks()
            .map { pagingData -> pagingData.map { it.toDomainModel() } }
    }
}
