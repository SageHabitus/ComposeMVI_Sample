package com.example.composemvi.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.composemvi.data.book.dummy.book.DummyBooks.getMockBookDomainModels
import com.example.composemvi.data.book.dummy.book.DummyBooks.getMockBookmarkedBookViewState
import com.example.composemvi.domain.model.BookDomainModel
import com.example.composemvi.domain.usecase.GetBookmarkedBooksUseCase
import com.example.composemvi.domain.usecase.ToggleBookmarkUseCase
import com.example.composemvi.presentation.ui.feature.bookmark.BookmarkEvent
import com.example.composemvi.presentation.ui.feature.bookmark.BookmarkIntent
import com.example.composemvi.presentation.ui.feature.bookmark.BookmarkedBookItemViewState
import com.example.composemvi.presentation.ui.feature.bookmark.BookmarkedBookViewModel
import com.example.composemvi.presentation.ui.feature.bookmark.BookmarkedBookViewState
import com.example.composemvi.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookmarkedBookViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BookmarkedBookViewModel
    private val getBookmarkedBooksUseCase: GetBookmarkedBooksUseCase = mockk()
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk()

    private lateinit var dummyBookDomainModels: List<BookDomainModel>
    private lateinit var dummyBookItemViewStates: List<BookmarkedBookItemViewState>

    @Before
    fun setUp() {
        viewModel = BookmarkedBookViewModel(
            getBookmarkedBooksUseCase = getBookmarkedBooksUseCase,
            toggleBookmarkUseCase = toggleBookmarkUseCase,
        )
        dummyBookItemViewStates = getMockBookmarkedBookViewState()
        dummyBookDomainModels = getMockBookDomainModels()
    }

    @Test
    fun `onIntent로 북마크된 책 목록 불러오기 시 정상 처리되는지 테스트`() = runTest {
        coEvery { getBookmarkedBooksUseCase.execute() } returns flowOf(PagingData.from(dummyBookDomainModels))

        viewModel.onIntent(BookmarkIntent.ShowBookmarkedBooks)
        viewModel.state.test {
            val state = awaitItem()
            assert((state.viewState as BookmarkedBookViewState.Success).books == dummyBookItemViewStates)
            assert(state.viewState == BookmarkedBookViewState.Loading)
        }
    }

    @Test
    fun `onIntent로 북마크 토글 시 정상 처리되는지 테스트`() = runTest {
        coEvery { toggleBookmarkUseCase.execute(any(), any()) } returns Unit

        viewModel.onIntent(BookmarkIntent.ToggleBookmark(dummyBookItemViewStates.first()))

        viewModel.event.test {
            val event = awaitItem()
            assert(event is BookmarkEvent.BookmarkActionEvent.ShowBookmarkRemovedSnackbar)
        }
    }

    @Test
    fun `onIntent로 북마크 토글 시 실패 이벤트 발생 테스트`() = runTest {
        coEvery { toggleBookmarkUseCase.execute(any(), any()) } throws Exception("북마크 실패")
        viewModel.onIntent(BookmarkIntent.ToggleBookmark(dummyBookItemViewStates.first()))
        viewModel.event.test {
            val event = awaitItem()
            assert(event is BookmarkEvent.BookmarkActionEvent.ShowBookmarkRemovedSnackbar)
        }
    }

    @Test
    fun `onIntent로 상세 페이지 이동 시 이벤트 발생 여부 확인`() = runTest {
        viewModel.onIntent(BookmarkIntent.NavigateToDetail(dummyBookItemViewStates.first()))
        viewModel.event.test {
            val event = awaitItem()
            assert(event is BookmarkEvent.NavigationEvent.NavigateToDetail)
        }
    }
}
