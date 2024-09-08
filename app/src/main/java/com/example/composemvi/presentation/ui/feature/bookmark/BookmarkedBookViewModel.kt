package com.example.composemvi.presentation.ui.feature.bookmark

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
import com.example.composemvi.domain.usecase.GetBookmarkedBooksUseCase
import com.example.composemvi.domain.usecase.ToggleBookmarkUseCase
import com.example.composemvi.presentation.mapper.toBookmarkViewState
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
class BookmarkedBookViewModel @Inject constructor(
    private val getBookmarkedBooksUseCase: GetBookmarkedBooksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
) : ViewModel() {

    val state: StateFlow<BookmarkedBookState>

    private val _event = Channel<BookmarkEvent>(Channel.UNLIMITED)
    val event: Flow<BookmarkEvent> = _event.receiveAsFlow()

    private val intent = MutableSharedFlow<BookmarkIntent>(extraBufferCapacity = 2)

    init {
        val initialState = BookmarkedBookState()

        state = dispatchIntentFlow()
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
            .toPartialStateChangeFlow()
            .sendSingleEvent()
            .scan(initialState) { state, change -> change.reduce(state) }
            .debugLog("BookmarkedBookState")
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialState)
    }

    @MainThread
    fun onIntent(intent: BookmarkIntent) = this.intent.tryEmit(intent)

    private fun dispatchIntentFlow(): Flow<BookmarkIntent> = merge(
        intent.filterIsInstance<BookmarkIntent.ShowBookmarkedBooks>().debounce(200),
        intent.filterIsInstance<BookmarkIntent.ToggleBookmark>().throttleFirst(200),
        intent.filterIsInstance<BookmarkIntent.NavigateToDetail>().throttleFirst(200),
    )

    private fun SharedFlow<BookmarkIntent>.toPartialStateChangeFlow(): Flow<BookmarkPartialStateChange> = merge(
        filterIsInstance<BookmarkIntent.ShowBookmarkedBooks>()
            .flatMapConcat { showBookmarkedBooks() },
        filterIsInstance<BookmarkIntent.ToggleBookmark>()
            .flatMapConcat { intent -> toggleBookmark(intent.book) },
        filterIsInstance<BookmarkIntent.NavigateToDetail>()
            .flatMapConcat { intent -> navigateToDetail(intent.book) },
    )

    private fun Flow<BookmarkPartialStateChange>.sendSingleEvent(): Flow<BookmarkPartialStateChange> =
        onEach { change ->
            val event = when (change) {
                is BookmarkPartialStateChange.BookmarkActionResult.Failed -> {
                    BookmarkEvent.BookmarkActionEvent.ShowBookmarkRemovedSnackbar(change.errorMessage)
                }

                is BookmarkPartialStateChange.NavigateToDetail.Success -> {
                    BookmarkEvent.NavigationEvent.NavigateToDetail(change.book)
                }

                else -> return@onEach
            }
            _event.trySend(event).getOrThrow()
        }

    private suspend fun showBookmarkedBooks(): Flow<BookmarkPartialStateChange> = getBookmarkedBooksUseCase
        .execute()
        .cachedIn(viewModelScope)
        .map { pagingData ->
            val cachedBooks = pagingData.map {
                it.toPresentationModel().toBookmarkViewState()
            }.asFlow()
            BookmarkPartialStateChange.BookmarksResult.Success(books = cachedBooks)
        }
        .startWith(BookmarkPartialStateChange.BookmarksResult.Loading)
        .catchMap { throwable ->
            BookmarkPartialStateChange.BookmarksResult.Failed(throwable.message)
        }

    private suspend fun toggleBookmark(book: BookmarkedBookItemViewState): Flow<BookmarkPartialStateChange> =
        toggleBookmarkUseCase
            .execute(book.isbn, book.isBookmarked)
            .asFlow()
            .map<Unit, BookmarkPartialStateChange> { BookmarkPartialStateChange.BookmarkActionResult.Success }
            .catchMap { throwable ->
                BookmarkPartialStateChange.BookmarkActionResult.Failed(throwable.message)
            }

    private fun navigateToDetail(book: BookmarkedBookItemViewState): Flow<BookmarkPartialStateChange> =
        BookmarkPartialStateChange.NavigateToDetail.Success(book = book)
            .asFlow()
            .map { it as BookmarkPartialStateChange }
            .catchMap { throwable ->
                BookmarkPartialStateChange.NavigateToDetail.Failed(throwable.message)
            }
}
