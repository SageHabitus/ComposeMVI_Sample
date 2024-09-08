package com.example.composemvi.presentation.ui.feature.booksearch

sealed class BookSearchIntent {
    data class SearchBooks(val query: String) : BookSearchIntent()
    data class ToggleBookmark(val bookItem: BookItemViewState) : BookSearchIntent()
    data class LoadInitialBooks(val query: String) : BookSearchIntent()
    data class NavigateToDetail(val bookItem: BookItemViewState) : BookSearchIntent()
    data class UpdateQuery(val query: String) : BookSearchIntent()
}
