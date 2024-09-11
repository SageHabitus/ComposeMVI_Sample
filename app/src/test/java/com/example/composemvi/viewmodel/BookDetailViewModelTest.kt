package com.example.composemvi.viewmodel

import app.cash.turbine.test
import com.example.composemvi.data.book.dummy.book.DummyBooks.getMockBookDetailViewState
import com.example.composemvi.domain.model.BookDomainModel
import com.example.composemvi.domain.usecase.GetBookUseCase
import com.example.composemvi.domain.usecase.ToggleBookmarkUseCase
import com.example.composemvi.presentation.ui.feature.bookdetail.BookDetailEvent
import com.example.composemvi.presentation.ui.feature.bookdetail.BookDetailIntent
import com.example.composemvi.presentation.ui.feature.bookdetail.BookDetailItemViewState
import com.example.composemvi.presentation.ui.feature.bookdetail.BookDetailViewModel
import com.example.composemvi.presentation.ui.feature.bookdetail.BookDetailViewState
import com.example.composemvi.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookDetailViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BookDetailViewModel
    private val getBookUseCase: GetBookUseCase = mockk()
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk()

    private lateinit var dummyBookDetailItemViewState: BookDetailItemViewState
    private lateinit var dummyBookDomainModel: BookDomainModel

    @Before
    fun setUp() {
        viewModel = BookDetailViewModel(
            getBookUseCase = getBookUseCase,
            toggleBookmarkUseCase = toggleBookmarkUseCase,
        )

        dummyBookDetailItemViewState = getMockBookDetailViewState()
    }

    @Test
    fun `onIntent로 책 상세 정보 조회 시 정상 처리되는지 테스트`() = runTest {
        coEvery { getBookUseCase.execute(any()) } returns dummyBookDomainModel

        viewModel.onIntent(BookDetailIntent.ShowBookDetails(dummyBookDetailItemViewState.isbn))

        viewModel.state.test {
            val state = awaitItem()
            assert((state.viewState as BookDetailViewState.Success).book == dummyBookDetailItemViewState)
        }
    }

    @Test
    fun `onIntent로 북마크 토글 시 정상 처리되는지 테스트`() = runTest {
        coEvery { toggleBookmarkUseCase.execute(any(), any()) } returns Unit

        viewModel.onIntent(BookDetailIntent.ToggleBookmark(dummyBookDetailItemViewState))
        viewModel.event.test {
            val event = awaitItem()
            assert(event is BookDetailEvent.BookmarkEvent.ShowBookmarkSuccessSnackbar)
        }
    }

    @Test
    fun `onIntent로 북마크 토글 시 실패 이벤트 처리 테스트`() = runTest {
        coEvery { toggleBookmarkUseCase.execute(any(), any()) } throws Exception("북마크 처리 실패")

        viewModel.onIntent(BookDetailIntent.ToggleBookmark(dummyBookDetailItemViewState))
        viewModel.event.test {
            val event = awaitItem()
            assert(event is BookDetailEvent.BookmarkEvent.ShowBookmarkFailedSnackbar)
        }
    }

    @Test
    fun `onIntent로 책 상세 정보 조회 실패 시 에러 처리 테스트`() = runTest {
        coEvery { getBookUseCase.execute(any()) } throws Exception("책 상세 정보 조회 실패")

        viewModel.onIntent(BookDetailIntent.ShowBookDetails(dummyBookDetailItemViewState.isbn))
        viewModel.event.test {
            val event = awaitItem()
            assert(event is BookDetailEvent.FetchResultEvent.ShowEmptyResultToast)
        }
    }
}
