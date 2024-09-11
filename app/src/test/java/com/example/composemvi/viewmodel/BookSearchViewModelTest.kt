import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.composemvi.data.book.dummy.book.DummyBooks.getMockBookDomainModels
import com.example.composemvi.data.book.dummy.book.DummyBooks.getMockBookSearchViewState
import com.example.composemvi.domain.model.BookDomainModel
import com.example.composemvi.domain.usecase.SearchBooksUseCase
import com.example.composemvi.domain.usecase.ToggleBookmarkUseCase
import com.example.composemvi.presentation.ui.feature.booksearch.BookItemViewState
import com.example.composemvi.presentation.ui.feature.booksearch.BookSearchEvent
import com.example.composemvi.presentation.ui.feature.booksearch.BookSearchIntent
import com.example.composemvi.presentation.ui.feature.booksearch.BookSearchViewModel
import com.example.composemvi.presentation.ui.feature.booksearch.BookSearchViewState
import com.example.composemvi.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookSearchViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BookSearchViewModel
    private val searchBooksUseCase: SearchBooksUseCase = mockk()
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase = mockk()

    private lateinit var dummyBookDomainModels: List<BookDomainModel>
    private lateinit var dummyBookItemViewStates: List<BookItemViewState>

    @Before
    fun setUp() {
        viewModel = BookSearchViewModel(
            searchBooksUseCase = searchBooksUseCase,
            toggleBookmarkUseCase = toggleBookmarkUseCase,
        )

        dummyBookItemViewStates = getMockBookSearchViewState()
        dummyBookDomainModels = getMockBookDomainModels()
    }

    @Test
    fun `onIntent로 검색 쿼리 업데이트 시 정상 처리되는지 테스트`() = runTest {
        viewModel.onIntent(BookSearchIntent.UpdateQuery("Kotlin"))

        viewModel.state.test {
            val state = awaitItem()
            assertEquals("Kotlin", state.query)
            assert(state.searchResultState is BookSearchViewState.Empty)
        }
    }

    @Test
    fun `onIntent로 책 검색 시 정상 처리되는지 테스트`() = runTest {
        coEvery { searchBooksUseCase.execute(any()) } returns flowOf(PagingData.from(dummyBookDomainModels))

        viewModel.onIntent(BookSearchIntent.SearchBooks("Kotlin"))

        viewModel.state.test {
            val loadingState = awaitItem()
            assert(loadingState.searchResultState is BookSearchViewState.Loading)

            val successState = awaitItem()
            assert(successState.searchResultState is BookSearchViewState.Success)
        }
    }

    @Test
    fun `onIntent로 북마크 토글 시 정상 처리되는지 테스트`() = runTest {
        coEvery { toggleBookmarkUseCase.execute(any(), any()) } returns Unit

        viewModel.onIntent(BookSearchIntent.ToggleBookmark(dummyBookItemViewStates.first()))

        viewModel.event.test {
            val event = awaitItem()
            assert(event is BookSearchEvent.BookmarkEvent.ShowBookmarkSuccessSnackbar)
        }
    }

    @Test
    fun `onIntent로 초기 책 목록 로드 시 정상 처리되는지 테스트`() = runTest {
        coEvery { searchBooksUseCase.execute(any()) } returns flowOf(PagingData.from(dummyBookDomainModels))

        viewModel.onIntent(BookSearchIntent.LoadInitialBooks("Kotlin"))

        viewModel.state.test {
            val loadingState = awaitItem()
            assert(loadingState.searchResultState is BookSearchViewState.Loading)

            val successState = awaitItem()
            assert(successState.searchResultState is BookSearchViewState.Success)
        }
    }

    @Test
    fun `onIntent로 상세 페이지 이동 시 이벤트 발생 여부 확인`() = runTest {
        viewModel.onIntent(BookSearchIntent.NavigateToDetail(dummyBookItemViewStates.first()))

        viewModel.event.test {
            val event = awaitItem()
            assert(event is BookSearchEvent.NavigationEvent.NavigateToDetail)
        }
    }
}
