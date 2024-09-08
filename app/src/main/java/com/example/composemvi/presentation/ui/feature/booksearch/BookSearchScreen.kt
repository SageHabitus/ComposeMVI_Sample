package com.example.composemvi.presentation.ui.feature.booksearch

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.composemvi.presentation.ui.common.AlertDialog
import com.example.composemvi.presentation.ui.common.BookImage
import com.example.composemvi.presentation.ui.common.LoadingIndicator
import com.example.composemvi.presentation.ui.common.showCustomToast
import com.example.composemvi_sample.R
import kotlinx.coroutines.flow.Flow

@Composable
fun BookSearchScreen(
    viewModel: BookSearchViewModel,
    snackbarState: SnackbarHostState,
    navController: NavHostController,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchField(
            searchText = state.query,
            onSearchTextChanged = { query -> viewModel.onIntent(BookSearchIntent.UpdateQuery(query)) },
            onSearchTriggered = { viewModel.onIntent(BookSearchIntent.SearchBooks(state.query)) },
        )

        when (state.searchResultState) {
            is BookSearchViewState.Loading -> LoadingIndicator()
            is BookSearchViewState.Failed -> Unit
            is BookSearchViewState.Success -> {
                BookSearchList(
                    bookItemUiState = (state.searchResultState as BookSearchViewState.Success).books,
                    onBookmarkClick = { book -> viewModel.onIntent(BookSearchIntent.ToggleBookmark(book)) },
                    onNavigateToDetail = { book -> viewModel.onIntent(BookSearchIntent.NavigateToDetail(book)) },
                )
            }

            BookSearchViewState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.search_empty),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }

    EventListener(
        event = viewModel.event,
        snackbarState = snackbarState,
        navController = navController,
    )
}

@Composable
fun BookSearchList(
    bookItemUiState: Flow<PagingData<BookItemViewState>>,
    onBookmarkClick: (BookItemViewState) -> Unit,
    onNavigateToDetail: (BookItemViewState) -> Unit,
) {
    val books = bookItemUiState.collectAsLazyPagingItems()

    LazyColumn {
        items(
            count = books.itemCount,
            key = { index -> books[index]?.isbn ?: index },
        ) { index ->
            books[index]?.let { book ->
                BookSearchListItem(
                    book = book,
                    onBookmarkClick = onBookmarkClick,
                    onClick = { onNavigateToDetail(book) },
                )
            }
        }
    }
}

@Composable
fun BookSearchListItem(book: BookItemViewState, onBookmarkClick: (BookItemViewState) -> Unit, onClick: () -> Unit) {
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
            Text(text = book.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = book.authors.joinToString(", "), style = MaterialTheme.typography.bodyMedium)
            Text(
                text = stringResource(id = R.string.publisher, book.publisher),
                style = MaterialTheme.typography.bodyMedium,
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
private fun SearchField(searchText: String, onSearchTextChanged: (String) -> Unit, onSearchTriggered: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { onSearchTextChanged(it) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text(text = stringResource(id = R.string.search_hint)) },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearchTriggered()
                },
            ),
        )

        Button(
            onClick = {
                keyboardController?.hide()
                onSearchTriggered()
            },
            modifier = Modifier.height(56.dp),
        ) {
            Text(text = stringResource(id = R.string.search))
        }
    }
}

@Composable
fun EventListener(event: Flow<BookSearchEvent>, snackbarState: SnackbarHostState, navController: NavHostController) {
    val context = LocalContext.current
    var currentDialogEvent by remember { mutableStateOf<BookSearchEvent.NavigationEvent?>(null) }

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is BookSearchEvent.NavigationEvent.ShowNavigationFailedDialog -> {
                    currentDialogEvent = event
                }

                is BookSearchEvent.NavigationEvent.NavigateToDetail -> {
                    val route = context.getString(R.string.navigate_to_book_detail, event.book.isbn)
                    navController.navigate(route)
                }

                is BookSearchEvent.BookmarkEvent.ShowBookmarkSuccessSnackbar -> {
                    snackbarState.showSnackbar(context.getString(R.string.bookmark_success))
                }

                is BookSearchEvent.BookmarkEvent.ShowBookmarkFailedSnackbar -> {
                    snackbarState.showSnackbar(context.getString(R.string.bookmark_failed))
                }

                is BookSearchEvent.SearchResultEvent.ShowEmptyResultToast -> {
                    showCustomToast(context, context.getString(R.string.search_empty))
                }

                is BookSearchEvent.SearchResultEvent.ShowSearchEndedToast -> {
                    showCustomToast(context, context.getString(R.string.search_ended))
                }
            }
        }
    }

    ShowBookSearchAlertDialog(
        currentDialogEvent,
        context,
        onConfirmClick = { currentDialogEvent = null },
    )
}

@Composable
private fun ShowBookSearchAlertDialog(
    dialogEvent: BookSearchEvent.NavigationEvent?,
    context: Context,
    onConfirmClick: () -> Unit,
) {
    dialogEvent?.let { event ->
        when (event) {
            is BookSearchEvent.NavigationEvent.NavigateToDetail -> {
                AlertDialog(
                    title = context.getString(R.string.navigation_success),
                    message = context.getString(R.string.navigation_message, event.book.title),
                    onConfirmClick = {
                        onConfirmClick()
                    },
                )
            }

            is BookSearchEvent.NavigationEvent.ShowNavigationFailedDialog -> {
                AlertDialog(
                    title = context.getString(R.string.navigation_failed),
                    message = event.message ?: context.getString(R.string.unknown_error),
                    onConfirmClick = { onConfirmClick() },
                )
            }
        }
    }
}
