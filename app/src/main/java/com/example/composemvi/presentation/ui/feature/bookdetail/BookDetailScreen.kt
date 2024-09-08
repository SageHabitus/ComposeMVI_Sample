package com.example.composemvi.presentation.ui.feature.bookdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composemvi.presentation.ui.common.LoadingIndicator
import com.example.composemvi.presentation.ui.common.NoResultsFound
import com.example.composemvi_sample.R
import kotlinx.coroutines.flow.Flow

@Composable
fun BookDetailScreen(
    isbn: String?,
    snackbarState: SnackbarHostState,
    viewModel: BookDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(isbn) {
        viewModel.onIntent(BookDetailIntent.ShowBookDetails(isbn))
    }

    println("디버깅 컴포즈: ${state.viewState}")
    when (state.viewState) {
        is BookDetailViewState.Loading -> {
            LoadingIndicator()
        }

        is BookDetailViewState.Success -> {
            val book = (state.viewState as BookDetailViewState.Success).book
            BookDetailContent(
                book = book,
                onBookmarkToggle = { viewModel.onIntent(BookDetailIntent.ToggleBookmark(book)) },
            )
        }

        is BookDetailViewState.Failed -> Unit
        is BookDetailViewState.Empty -> NoResultsFound()
    }

    BookDetailEventListener(
        event = viewModel.event,
        snackbarState = snackbarState,
    )
}

@Composable
fun BookDetailEventListener(event: Flow<BookDetailEvent>, snackbarState: SnackbarHostState) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is BookDetailEvent.BookmarkEvent.ShowBookmarkSuccessSnackbar -> {
                    snackbarState.showSnackbar(context.getString(R.string.bookmark_success))
                }

                is BookDetailEvent.BookmarkEvent.ShowBookmarkFailedSnackbar -> {
                    snackbarState.showSnackbar(context.getString(R.string.bookmark_failed))
                }

                is BookDetailEvent.FetchResultEvent.ShowEmptyResultToast -> {
                    snackbarState.showSnackbar(event.message ?: "")
                }

                is BookDetailEvent.FetchResultEvent.ShowSuccessToast -> {
                    snackbarState.showSnackbar(context.getString(R.string.bookmark_success))
                }
            }
        }
    }
}

@Composable
fun BookDetailContent(book: BookDetailItemViewState, onBookmarkToggle: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        BasicText(text = stringResource(id = R.string.title, book.title))
        Spacer(modifier = Modifier.size(8.dp))
        BasicText(text = stringResource(id = R.string.authors, book.authors.joinToString()))
        Spacer(modifier = Modifier.size(8.dp))
        BasicText(text = stringResource(id = R.string.publisher_text, book.publisher))
        Spacer(modifier = Modifier.size(8.dp))
        BasicText(text = stringResource(id = R.string.price, book.price))
        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = onBookmarkToggle,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = if (book.isBookmarked) {
                    stringResource(
                        id = R.string.remove_bookmark,
                    )
                } else {
                    stringResource(id = R.string.add_bookmark)
                },
            )
        }
    }
}
