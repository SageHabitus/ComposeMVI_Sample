package com.example.composemvi.data.book.dummy.book

import com.example.composemvi.data.book.dummy.loader.TestResourceLoader.getJsonStringFromResource
import com.example.composemvi.data.book.dummy.loader.TestResourceLoader.getListFromResource
import com.example.composemvi.data.mapper.toDataModel
import com.example.composemvi.data.source.local.entity.BookEntity
import com.example.composemvi.domain.mapper.toDomainModel
import com.example.composemvi.presentation.mapper.toBookItemViewState
import com.example.composemvi.presentation.mapper.toBookmarkViewState
import com.example.composemvi.presentation.mapper.toDetailBookViewState
import com.example.composemvi.presentation.mapper.toPresentationModel
import okhttp3.mockwebserver.MockResponse

object DummyBooks {

    private const val BOOK_REMOTE_FILE_NAME = "dummy_books_remote.json"
    private const val BOOK_LOCAL_FILE_NAME = "dummy_books_local.json"

    fun getMockBookEntities(): List<BookEntity> = getListFromResource<BookEntity>(BOOK_LOCAL_FILE_NAME)

    fun getMockBookResponse(code: Int = 200): MockResponse = MockResponse()
        .setBody(getJsonStringFromResource(BOOK_REMOTE_FILE_NAME))
        .setResponseCode(code)
        .addHeader("Content-Type", "application/json")

    fun getMockBookDataModels() = getMockBookEntities().map { it.toDataModel() }
    fun getMockBookDomainModels() = getMockBookDataModels().map { it.toDomainModel() }
    fun getMockBookPresentationModels() = getMockBookDataModels().map { it.toPresentationModel() }
    fun getMockBookSearchViewState() = getMockBookPresentationModels().map { it.toBookItemViewState() }
    fun getMockBookmarkedBookViewState() = getMockBookPresentationModels().map { it.toBookmarkViewState() }
    fun getMockBookDetailViewState() = getMockBookPresentationModels().first().toDetailBookViewState()
}
