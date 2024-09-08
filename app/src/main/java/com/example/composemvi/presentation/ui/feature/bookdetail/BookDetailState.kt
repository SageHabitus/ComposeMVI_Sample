package com.example.composemvi.presentation.ui.feature.bookdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class BookDetailState(
    val viewState: BookDetailViewState = BookDetailViewState.Loading,
)

sealed interface BookDetailViewState {
    data class Success(val book: BookDetailItemViewState) : BookDetailViewState
    data class Failed(val message: String?) : BookDetailViewState
    data object Loading : BookDetailViewState
    data object Empty : BookDetailViewState
}

@Parcelize
data class BookDetailItemViewState(
    val isbn: String,
    val title: String,
    val authors: List<String>,
    val publisher: String,
    val thumbnail: String,
    val price: Int,
    val salePrice: Int,
    val url: String,
    val contents: String,
    val isBookmarked: Boolean,
) : Parcelable
