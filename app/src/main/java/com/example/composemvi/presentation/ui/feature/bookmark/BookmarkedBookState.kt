package com.example.composemvi.presentation.ui.feature.bookmark

import android.os.Parcelable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize

data class BookmarkedBookState(
    val viewState: BookmarkedBookViewState = BookmarkedBookViewState.Empty,
)

sealed interface BookmarkedBookViewState {
    data class Success(val books: Flow<PagingData<BookmarkedBookItemViewState>>) : BookmarkedBookViewState
    data class Failed(val message: String?) : BookmarkedBookViewState
    data object Loading : BookmarkedBookViewState
    data object Empty : BookmarkedBookViewState
}

@Parcelize
data class BookmarkedBookItemViewState(
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
