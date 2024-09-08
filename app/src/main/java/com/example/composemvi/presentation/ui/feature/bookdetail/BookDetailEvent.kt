package com.example.composemvi.presentation.ui.feature.bookdetail

sealed interface BookDetailEvent {
    sealed interface FetchResultEvent : BookDetailEvent {
        data class ShowEmptyResultToast(val message: String?) : BookDetailEvent
        data object ShowSuccessToast : BookDetailEvent
    }

    sealed interface BookmarkEvent : BookDetailEvent {
        data object ShowBookmarkSuccessSnackbar : BookmarkEvent
        data object ShowBookmarkFailedSnackbar : BookmarkEvent
    }
}
