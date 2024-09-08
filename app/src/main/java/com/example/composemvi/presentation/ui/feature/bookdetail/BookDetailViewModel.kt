package com.example.composemvi.presentation.ui.feature.bookdetail

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemvi.core.extension.asFlow
import com.example.composemvi.core.extension.catchMap
import com.example.composemvi.core.extension.throttleFirst
import com.example.composemvi.domain.usecase.GetBookUseCase
import com.example.composemvi.domain.usecase.ToggleBookmarkUseCase
import com.example.composemvi.presentation.mapper.toDetailBookViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
class BookDetailViewModel @Inject constructor(
    private val getBookUseCase: GetBookUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
) : ViewModel() {

    val state: StateFlow<BookDetailState>

    private val _event = Channel<BookDetailEvent>(Channel.UNLIMITED)
    val event: Flow<BookDetailEvent> = _event.receiveAsFlow()

    private val intent = MutableSharedFlow<BookDetailIntent>(extraBufferCapacity = 2)

    init {
        val initialState = BookDetailState()

        state = dispatchIntentFlow()
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
            .toPartialStateChangeFlow()
            .sendSingleEvent()
            .scan(initialState) { state, change -> change.reduce(state) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, initialState)
    }

    @MainThread
    fun onIntent(intent: BookDetailIntent) {
        this.intent.tryEmit(intent)
    }

    private fun dispatchIntentFlow(): Flow<BookDetailIntent> = merge(
        intent.filterIsInstance<BookDetailIntent.ShowBookDetails>(),
        intent.filterIsInstance<BookDetailIntent.ToggleBookmark>().throttleFirst(300),
    )

    private fun Flow<BookDetailIntent>.toPartialStateChangeFlow(): Flow<BookDetailPartialStateChange> = merge(
        filterIsInstance<BookDetailIntent.ShowBookDetails>()
            .flatMapConcat { intent -> loadBookDetails(intent.isbn) },
        filterIsInstance<BookDetailIntent.ToggleBookmark>()
            .flatMapConcat { intent -> toggleBookmark(intent.book) },
    )

    private fun Flow<BookDetailPartialStateChange>.sendSingleEvent(): Flow<BookDetailPartialStateChange> {
        return onEach { change ->
            val event = when (change) {
                is BookDetailPartialStateChange.BookmarkToggle.Failed -> {
                    BookDetailEvent.BookmarkEvent.ShowBookmarkFailedSnackbar
                }

                is BookDetailPartialStateChange.BookmarkToggle.Success -> {
                    BookDetailEvent.BookmarkEvent.ShowBookmarkSuccessSnackbar
                }

                is BookDetailPartialStateChange.FetchBook.Failed -> {
                    BookDetailEvent.FetchResultEvent.ShowEmptyResultToast(change.errorMessage)
                }

                is BookDetailPartialStateChange.FetchBook.Success -> {
                    BookDetailEvent.FetchResultEvent.ShowSuccessToast
                }
            }
            _event.trySend(event).getOrThrow()
        }
    }

    private suspend fun loadBookDetails(isbn: String?): Flow<BookDetailPartialStateChange> = if (isbn == null) {
        BookDetailPartialStateChange.FetchBook.Failed().asFlow()
    } else {
        getBookUseCase
            .execute(isbn)
            .toDetailBookViewState()
            .asFlow()
            .map<BookDetailItemViewState, BookDetailPartialStateChange> { book ->
                BookDetailPartialStateChange.FetchBook.Success(book)
            }
            .catchMap { throwable ->
                BookDetailPartialStateChange.FetchBook.Failed(throwable.message)
            }
    }

    private suspend fun toggleBookmark(book: BookDetailItemViewState): Flow<BookDetailPartialStateChange> =
        toggleBookmarkUseCase
            .execute(book.isbn, book.isBookmarked)
            .asFlow()
            .map<Unit, BookDetailPartialStateChange> {
                val updatedBook = book.copy(isBookmarked = !book.isBookmarked)
                BookDetailPartialStateChange.BookmarkToggle.Success(updatedBook)
            }
            .catchMap { throwable ->
                BookDetailPartialStateChange.BookmarkToggle.Failed(throwable.message)
            }
}
