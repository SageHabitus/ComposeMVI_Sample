package com.example.composemvi.presentation.ui.feature.bookmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.composemvi.presentation.ui.common.BookImage
import com.example.composemvi.presentation.ui.common.LoadingIndicator
import com.example.composemvi_sample.R
import kotlinx.coroutines.flow.Flow

@Composable
fun BookmarkedBookScreen(
    viewModel: BookmarkedBookViewModel,
    snackbarState: SnackbarHostState,
    navController: NavHostController,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onIntent(BookmarkIntent.ShowBookmarkedBooks)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when (val viewState = state.viewState) {
            is BookmarkedBookViewState.Loading -> LoadingIndicator()
            is BookmarkedBookViewState.Failed -> Unit
            is BookmarkedBookViewState.Success -> {
                BookmarkedBookList(
                    bookItemUiState = viewState.books,
                    onBookmarkClick = { book -> viewModel.onIntent(BookmarkIntent.ToggleBookmark(book)) },
                    onNavigateToDetail = { book -> viewModel.onIntent(BookmarkIntent.NavigateToDetail(book)) },
                )
            }

            BookmarkedBookViewState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.no_bookmarked_books),
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }

    BookmarkedBookEventListener(
        event = viewModel.event,
        snackbarState = snackbarState,
        navController = navController,
    )
}

@Composable
fun BookmarkedBookList(
    bookItemUiState: Flow<PagingData<BookmarkedBookItemViewState>>,
    onBookmarkClick: (BookmarkedBookItemViewState) -> Unit,
    onNavigateToDetail: (BookmarkedBookItemViewState) -> Unit,
) {
    val books = bookItemUiState.collectAsLazyPagingItems()

    LazyColumn {
        items(books.itemCount) { index ->
            books[index]?.let { book ->
                BookmarkedBookListItem(
                    book = book,
                    onBookmarkClick = onBookmarkClick,
                    onClick = { onNavigateToDetail(book) },
                )
            }
        }
    }
}

@Composable
fun BookmarkedBookListItem(
    book: BookmarkedBookItemViewState,
    onBookmarkClick: (BookmarkedBookItemViewState) -> Unit,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
    ) {
        BookImage(
            painter = rememberAsyncImagePainter(
                model = book.thumbnail,
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.High,
            ),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = book.title, style = androidx.compose.material3.MaterialTheme.typography.bodyLarge)
            Text(
                text = book.authors.joinToString(", "),
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(id = R.string.publisher, book.publisher),
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            )
        }

        IconButton(onClick = { onBookmarkClick(book) }) {
            Icon(
                imageVector = if (book.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun BookmarkedBookEventListener(
    event: Flow<BookmarkEvent>,
    snackbarState: SnackbarHostState,
    navController: NavHostController,
) {
    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is BookmarkEvent.BookmarkActionEvent.ShowBookmarkRemovedSnackbar -> {
                    snackbarState.showSnackbar(event.message ?: "")
                }

                is BookmarkEvent.NavigationEvent.NavigateToDetail -> {
                    val route = navController.context.getString(R.string.navigate_to_book_detail, event.book.isbn)
                    navController.navigate(route)
                }

                is BookmarkEvent.BookmarkActionEvent.ShowBookmarkFailedSnackbar -> return@collect
            }
        }
    }
}
