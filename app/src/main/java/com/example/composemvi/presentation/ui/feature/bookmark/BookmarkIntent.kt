package com.example.composemvi.presentation.ui.feature.bookmark

sealed class BookmarkIntent {
    data object ShowBookmarkedBooks : BookmarkIntent()
    data class ToggleBookmark(val book: BookmarkedBookItemViewState) : BookmarkIntent()
    data class NavigateToDetail(val book: BookmarkedBookItemViewState) : BookmarkIntent()
}
