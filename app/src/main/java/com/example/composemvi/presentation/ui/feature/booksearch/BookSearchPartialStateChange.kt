package com.example.composemvi.presentation.ui.feature.booksearch

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

sealed interface BookSearchPartialStateChange {
    fun reduce(oldState: BookSearchState): BookSearchState

    sealed interface SearchResult : BookSearchPartialStateChange {
        override fun reduce(oldState: BookSearchState): BookSearchState {
            return when (this) {
                is Success -> oldState.copy(
                    searchResultState = BookSearchViewState.Success(books),
                )

                is Failed -> oldState.copy(
                    searchResultState = BookSearchViewState.Failed(errorMessage),
                )

                is Loading -> oldState.copy(
                    searchResultState = BookSearchViewState.Loading,
                )
            }
        }

        data class Success(val books: Flow<PagingData<BookItemViewState>>) : SearchResult
        data class Failed(val errorMessage: String? = null) : SearchResult
        data object Loading : SearchResult
    }

    sealed interface BookmarkResult : BookSearchPartialStateChange {
        override fun reduce(oldState: BookSearchState): BookSearchState {
            return when (this) {
                is Success -> oldState.copy(
                    searchResultState = oldState.searchResultState,
                )

                is Failed -> oldState.copy(
                    searchResultState = BookSearchViewState.Failed(errorMessage),
                )

                is Loading -> oldState.copy(
                    searchResultState = BookSearchViewState.Loading,
                )
            }
        }

        data object Success : BookmarkResult
        data class Failed(val errorMessage: String? = null) : BookmarkResult
        data object Loading : BookmarkResult
    }

    sealed interface UpdateQuery : BookSearchPartialStateChange {
        override fun reduce(oldState: BookSearchState): BookSearchState {
            return when (this) {
                is Success -> oldState.copy(
                    query = query,
                    searchResultState = oldState.searchResultState,
                )

                is Failed -> oldState.copy(
                    searchResultState = BookSearchViewState.Failed(""),
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
                    searchResultState = oldState.searchResultState,
                )

                is Failed -> oldState.copy(
                    searchResultState = BookSearchViewState.Failed(errorMessage),
                )
            }
        }

        data class Success(val book: BookItemViewState) : NavigateToDetail
        data class Failed(val errorMessage: String? = null) : NavigateToDetail
    }
}
