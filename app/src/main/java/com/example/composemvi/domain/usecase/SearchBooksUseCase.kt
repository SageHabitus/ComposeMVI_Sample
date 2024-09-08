package com.example.composemvi.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.composemvi.data.repository.BookRepository
import com.example.composemvi.domain.exception.DomainExceptionMapper
import com.example.composemvi.domain.mapper.toDomainModel
import com.example.composemvi.domain.model.BookDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val bookRepo: BookRepository,
) {
    suspend fun execute(query: String): Flow<PagingData<BookDomainModel>> {
        return bookRepo.searchAndCacheBooks(query)
            .map { pagingData ->
                pagingData.map { it.toDomainModel() }
            }
            .catch { exception ->
                throw DomainExceptionMapper.toDomainException(exception)
            }
    }
}
