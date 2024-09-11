package com.example.composemvi.presentation.ui.feature.booksearch

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

sealed interface BookSearchPartialStateChange {
    fun reduce(oldState: BookSearchState): BookSearchState

    sealed interface LoadingDialog : BookSearchPartialStateChange {
        data object Show : LoadingDialog {
            override fun reduce(oldState: BookSearchState) = oldState.copy(
                viewState = oldState.viewState,
            )
        }
    }

    sealed interface SearchResult : BookSearchPartialStateChange {
        override fun reduce(oldState: BookSearchState): BookSearchState {
            return when (this) {
                is Success -> oldState.copy(
                    viewState = BookSearchViewState.Success(books),
                )

                is Failed -> oldState.copy(
                    viewState = BookSearchViewState.Failed(errorMessage),
                )
            }
        }

        data class Success(val books: Flow<PagingData<BookItemViewState>>) : SearchResult
        data class Failed(val errorMessage: String? = null) : SearchResult
    }

    sealed interface BookmarkResult : BookSearchPartialStateChange {
        override fun reduce(oldState: BookSearchState): BookSearchState {
            return when (this) {
                is Success -> oldState.copy(
                    viewState = oldState.viewState,
                )

                is Failed -> oldState.copy(
                    viewState = BookSearchViewState.Failed(errorMessage),
                )
            }
        }

        data object Success : BookmarkResult
        data class Failed(val errorMessage: String? = null) : BookmarkResult
    }

    sealed interface UpdateQuery : BookSearchPartialStateChange {
        override fun reduce(oldState: BookSearchState): BookSearchState {
            return when (this) {
                is Success -> oldState.copy(
                    query = query,
                    viewState = oldState.viewState,
                )

                is Failed -> oldState.copy(
                    viewState = BookSearchViewState.Failed(""),
                )
            }
        }

        data class Success(val query: String) : UpdateQuery
        data class Failed(val errorMessage: String? = null) : UpdateQuery
    }

    sealed interface NavigateToDetail : BookSearchPartialStateChange {
        override fun reduce(oldState: BookSearchState): BookSearchState {
            return when (this) {
                is Success -> oldState.copy(
                    viewState = oldState.viewState,
                )

                is Failed -> oldState.copy(
                    viewState = BookSearchViewState.Failed(errorMessage),
                )
            }
        }

        data class Success(val book: BookItemViewState) : NavigateToDetail
        data class Failed(val errorMessage: String? = null) : NavigateToDetail
    }
}
