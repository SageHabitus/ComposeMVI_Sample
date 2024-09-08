package com.example.composemvi.presentation.ui.feature.bookdetail

sealed class BookDetailIntent {
    data class ShowBookDetails(val isbn: String?) : BookDetailIntent()
    data class ToggleBookmark(val book: BookDetailItemViewState) : BookDetailIntent()
}
