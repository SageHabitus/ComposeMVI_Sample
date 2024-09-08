package com.example.composemvi.presentation.ui.feature.booksearch

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.composemvi.core.extension.asFlow
import com.example.composemvi.core.extension.catchMap
import com.example.composemvi.core.extension.debugLog
import com.example.composemvi.core.extension.startWith
import com.example.composemvi.core.extension.throttleFirst
import com.example.composemvi.domain.usecase.SearchBooksUseCase
import com.example.composemvi.domain.usecase.ToggleBookmarkUseCase
import com.example.composemvi.presentation.mapper.toBookItemViewState
import com.example.composemvi.presentation.mapper.toPresentationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
) : ViewModel() {

    val state: StateFlow<BookSearchState>

    private val _event = Channel<BookSearchEvent>(Channel.UNLIMITED)
    val event: Flow<BookSearchEvent> = _event.receiveAsFlow()

    private val intent = MutableSharedFlow<BookSearchIntent>(extraBufferCapacity = 2)

    init {
        val initViewState = BookSearchState()

        state = dispatchIntentFlow()
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
            .toSearchPartialStateChangeFlow()
            .sendSingleEvent()
            .scan(initViewState) { state, change -> change.reduce(state) }
            .debugLog("BookSearchState")
            .stateIn(viewModelScope, SharingStarted.Eagerly, initViewState)
    }

    @MainThread
    fun onIntent(intent: BookSearchIntent) = this.intent.tryEmit(intent)

    private fun dispatchIntentFlow(): Flow<BookSearchIntent> = merge(
        intent.filterIsInstance<BookSearchIntent.LoadInitialBooks>().debounce(200),
        intent.filterIsInstance<BookSearchIntent.SearchBooks>().debounce(200),
        intent.filterIsInstance<BookSearchIntent.ToggleBookmark>().throttleFirst(200),
        intent.filterIsInstance<BookSearchIntent.NavigateToDetail>().throttleFirst(200),
        intent.filterIsInstance<BookSearchIntent.UpdateQuery>(),
    )

    private fun SharedFlow<BookSearchIntent>.toSearchPartialStateChangeFlow(): Flow<BookSearchPartialStateChange> =
        merge(
            filterIsInstance<BookSearchIntent.LoadInitialBooks>()
                .flatMapConcat { intent -> loadInitialBooks(intent.query) },
            filterIsInstance<BookSearchIntent.UpdateQuery>()
                .flatMapConcat { intent -> updateQuery(intent.query) },
            filterIsInstance<BookSearchIntent.SearchBooks>()
                .flatMapConcat { intent -> searchBooks(intent.query) },
            filterIsInstance<BookSearchIntent.ToggleBookmark>()
                .flatMapConcat { intent -> toggleBookmark(intent.bookItem) },
            filterIsInstance<BookSearchIntent.NavigateToDetail>()
                .flatMapConcat { intent -> navigateToDetail(intent.bookItem) },
        )

    private fun Flow<BookSearchPartialStateChange>.sendSingleEvent(): Flow<BookSearchPartialStateChange> =
        onEach { change ->
            val event = when (change) {
                is BookSearchPartialStateChange.BookmarkResult.Success -> {
                    BookSearchEvent.BookmarkEvent.ShowBookmarkSuccessSnackbar
                }

                is BookSearchPartialStateChange.BookmarkResult.Failed -> {
                    BookSearchEvent.BookmarkEvent.ShowBookmarkFailedSnackbar
                }

                is BookSearchPartialStateChange.SearchResult.Failed -> {
                    BookSearchEvent.SearchResultEvent.ShowEmptyResultToast
                }

                is BookSearchPartialStateChange.NavigateToDetail.Success -> {
                    BookSearchEvent.NavigationEvent.NavigateToDetail(change.book)
                }

                is BookSearchPartialStateChange.NavigateToDetail.Failed -> {
                    BookSearchEvent.NavigationEvent.ShowNavigationFailedDialog(change.errorMessage)
                }

                else -> return@onEach
            }
            _event.trySend(event).getOrThrow()
        }

    private suspend fun loadInitialBooks(query: String): Flow<BookSearchPartialStateChange> = searchBooks("")

    private fun updateQuery(query: String): Flow<BookSearchPartialStateChange> =
        BookSearchPartialStateChange.UpdateQuery.Success(query = query)
            .asFlow()
            .map { it as BookSearchPartialStateChange }
            .catchMap { throwable ->
                BookSearchPartialStateChange.UpdateQuery.Failed(
                    throwable.message,
                )
            }

    private suspend fun searchBooks(query: String): Flow<BookSearchPartialStateChange> = searchBooksUseCase
        .execute(query)
        .cachedIn(viewModelScope)
        .map { pagingData ->
            val cachedBooks = pagingData.map {
                it.toPresentationModel().toBookItemViewState()
            }.asFlow()
            BookSearchPartialStateChange.SearchResult.Success(books = cachedBooks)
        }
        .startWith(BookSearchPartialStateChange.SearchResult.Loading)
        .catchMap { throwable ->
            BookSearchPartialStateChange.SearchResult.Failed(
                throwable.message,
            )
        }

    private suspend fun toggleBookmark(bookItem: BookItemViewState): Flow<BookSearchPartialStateChange> =
        toggleBookmarkUseCase
            .execute(bookItem.isbn, bookItem.isBookmarked)
            .asFlow()
            .map<Unit, BookSearchPartialStateChange> { BookSearchPartialStateChange.BookmarkResult.Success }
            .catchMap { throwable ->
                BookSearchPartialStateChange.BookmarkResult.Failed(
                    throwable.message,
                )
            }

    private fun navigateToDetail(bookItem: BookItemViewState): Flow<BookSearchPartialStateChange> =
        BookSearchPartialStateChange.NavigateToDetail.Success(book = bookItem)
            .asFlow()
            .map { it as BookSearchPartialStateChange }
            .catchMap { throwable ->
                BookSearchPartialStateChange.NavigateToDetail.Failed(
                    throwable.message,
                )
            }
}
