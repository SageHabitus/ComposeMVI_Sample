package com.example.composemvi.presentation.ui.feature.bookdetail

sealed interface BookDetailEvent {
    sealed interface FetchResultEvent : BookDetailEvent {
        data object ShowEmptyResultToast : BookDetailEvent
        data object ShowSuccessToast : BookDetailEvent
    }

    sealed interface BookmarkEvent : BookDetailEvent {
        data object ShowBookmarkSuccessSnackbar : BookmarkEvent
        data object ShowBookmarkFailedSnackbar : BookmarkEvent
    }
}
