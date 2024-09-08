package com.example.composemvi.presentation.ui.feature.bookdetail

sealed interface BookDetailPartialStateChange {
    fun reduce(oldState: BookDetailState): BookDetailState

    sealed interface FetchBook : BookDetailPartialStateChange {
        override fun reduce(oldState: BookDetailState): BookDetailState {
            return oldState
        }

        data class Success(val book: BookDetailItemViewState) : FetchBook
        data class Failed(val errorMessage: String? = null) : FetchBook
    }

    sealed interface BookmarkToggle : BookDetailPartialStateChange {
        override fun reduce(oldState: BookDetailState): BookDetailState {
            return when (this) {
                is Success -> oldState.copy(
                    viewState = oldState.viewState,
                )

                is Failed -> oldState.copy(
                    viewState = BookDetailViewState.Failed(errorMessage),
                )
            }
        }

        data object Success : BookmarkToggle
        data class Failed(val errorMessage: String? = null) : BookmarkToggle
    }
}
