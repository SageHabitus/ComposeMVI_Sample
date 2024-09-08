package com.example.composemvi.presentation.mapper

import com.example.composemvi.presentation.model.BookPresentationModel
import com.example.composemvi.presentation.ui.feature.bookmark.BookmarkedBookItemViewState
import com.example.composemvi.presentation.ui.feature.booksearch.BookItemViewState

fun BookItemViewState.toPresentationModel(): BookPresentationModel {
    return BookPresentationModel(
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

fun BookmarkedBookItemViewState.toPresentationModel(): BookPresentationModel {
    return BookPresentationModel(
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
