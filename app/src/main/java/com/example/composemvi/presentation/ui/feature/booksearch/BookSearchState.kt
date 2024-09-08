package com.example.composemvi.presentation.ui.feature.booksearch

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize

data class BookSearchState(
    val query: String = "",
    val searchResultState: BookSearchViewState = BookSearchViewState.Empty,
)

sealed interface BookSearchViewState {
    data class Success(val books: Flow<PagingData<BookItemViewState>>) : BookSearchViewState
    data class Failed(val message: String?) : BookSearchViewState
    data object Loading : BookSearchViewState
    data object Empty : BookSearchViewState
}

@Parcelize
data class BookItemViewState(
    val isbn: String,
    val title: String,
    val authors: List<String>,
    val publisher: String,
    val thumbnail: String,
    val contents: String,
    val price: Int,
    val salePrice: Int,
    val url: String,
    val isBookmarked: Boolean,
) : Parcelable
