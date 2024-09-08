package com.example.composemvi.presentation.ui.feature.bookmark

sealed interface BookmarkEvent {

    sealed interface BookmarkActionEvent : BookmarkEvent {
        data class ShowBookmarkRemovedSnackbar(val message: String?) : BookmarkActionEvent
        data class ShowBookmarkFailedSnackbar(val message: String?) : BookmarkActionEvent
    }

    sealed interface NavigationEvent : BookmarkEvent {
        data class NavigateToDetail(val book: BookmarkedBookItemViewState) : NavigationEvent
        data class ShowNavigationFailedDialog(val message: String?) : NavigationEvent
    }
}
