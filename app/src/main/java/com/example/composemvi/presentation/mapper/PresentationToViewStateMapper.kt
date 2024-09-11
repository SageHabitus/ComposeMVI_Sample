package com.example.composemvi.presentation.mapper

import com.example.composemvi.presentation.model.BookPresentationModel
import com.example.composemvi.presentation.ui.feature.bookdetail.BookDetailItemViewState
import com.example.composemvi.presentation.ui.feature.bookmark.BookmarkedBookItemViewState
import com.example.composemvi.presentation.ui.feature.booksearch.BookItemViewState

fun BookPresentationModel.toBookItemViewState(): BookItemViewState {
    return BookItemViewState(
        isbn = isbn,
        title = title,
        authors = authors,
        contents = contents,
        publisher = publisher,
        price = price,
        salePrice = salePrice,
        thumbnail = thumbnail,
        url = url,
        isBookmarked = isBookmarked,
    )
}

fun BookPresentationModel.toBookmarkViewState(): BookmarkedBookItemViewState {
    return BookmarkedBookItemViewState(
        isbn = isbn,
        title = title,
        authors = authors,
        contents = contents,
        publisher = publisher,
        price = price,
        salePrice = salePrice,
        thumbnail = thumbnail,
        url = url,
        isBookmarked = isBookmarked,
    )
}

fun BookPresentationModel.toDetailBookViewState(): BookDetailItemViewState {
    return BookDetailItemViewState(
        isbn = isbn,
        title = title,
        authors = authors,
        contents = contents,
        publisher = publisher,
        price = price,
        salePrice = salePrice,
        thumbnail = thumbnail,
        url = url,
        isBookmarked = isBookmarked,
    )
}
