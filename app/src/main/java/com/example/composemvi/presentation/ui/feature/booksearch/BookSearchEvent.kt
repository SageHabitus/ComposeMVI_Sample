package com.example.composemvi.presentation.ui.feature.booksearch

sealed interface BookSearchEvent {
    sealed interface BookmarkEvent : BookSearchEvent {
        data object ShowBookmarkSuccessSnackbar : BookmarkEvent
        data object ShowBookmarkFailedSnackbar : BookmarkEvent
    }

    sealed interface SearchResultEvent : BookSearchEvent {
        data object ShowEmptyResultToast : SearchResultEvent
        data object ShowSearchEndedToast : SearchResultEvent
    }

    sealed interface NavigationEvent : BookSearchEvent {
        data class NavigateToDetail(val book: BookItemViewState) : NavigationEvent
        data class ShowNavigationFailedDialog(val message: String?) : NavigationEvent
    }
}
