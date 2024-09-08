package com.example.composemvi.presentation.ui.feature.bookmark

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

sealed interface BookmarkPartialStateChange {
    fun reduce(oldState: BookmarkedBookState): BookmarkedBookState

    sealed interface BookmarksResult : BookmarkPartialStateChange {
        override fun reduce(oldState: BookmarkedBookState): BookmarkedBookState {
            return when (this) {
                is Success -> oldState.copy(
                    viewState = BookmarkedBookViewState.Success(books),
                )

                is Failed -> oldState.copy(
                    viewState = BookmarkedBookViewState.Failed(errorMessage),
                )

                is Loading -> oldState.copy(
                    viewState = BookmarkedBookViewState.Loading,
                )
            }
        }

        data class Success(val books: Flow<PagingData<BookmarkedBookItemViewState>>) : BookmarksResult
        data class Failed(val errorMessage: String? = null) : BookmarksResult
        data object Loading : BookmarksResult
    }

    sealed interface BookmarkActionResult : BookmarkPartialStateChange {
        override fun reduce(oldState: BookmarkedBookState): BookmarkedBookState {
            return when (this) {
                Success -> oldState.copy(
                    viewState = oldState.viewState,
                )

                is Failed -> oldState.copy(
                    viewState = BookmarkedBookViewState.Failed(errorMessage),
                )
            }
        }

        data object Success : BookmarkActionResult
        data class Failed(val errorMessage: String? = null) : BookmarkActionResult
    }

    sealed interface NavigateToDetail : BookmarkPartialStateChange {
        override fun reduce(oldState: BookmarkedBookState): BookmarkedBookState {
            return when (this) {
                is Success -> oldState.copy(
                    viewState = oldState.viewState,
                )

                is Failed -> oldState.copy(
                    viewState = BookmarkedBookViewState.Failed(errorMessage),
                )
            }
        }

        data class Success(val book: BookmarkedBookItemViewState) : NavigateToDetail
        data class Failed(val errorMessage: String? = null) : NavigateToDetail
    }
}
