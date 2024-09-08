package com.example.composemvi.presentation.mapper

import com.example.composemvi.data.model.BookDataModel
import com.example.composemvi.domain.model.BookDomainModel
import com.example.composemvi.presentation.model.BookPresentationModel
import com.example.composemvi.presentation.ui.feature.bookdetail.BookDetailItemViewState

fun BookDomainModel.toPresentationModel(): BookPresentationModel {
    return BookPresentationModel(
        title = title,
        authors = authors,
        publisher = publisher,
        thumbnail = thumbnail,
        contents = contents,
        isbn = isbn,
        price = price,
        salePrice = salePrice,
        url = url,
        isBookmarked = isBookmarked,
    )
}

fun BookDataModel.toPresentationModel(): BookPresentationModel {
    return BookPresentationModel(
        title = title,
        authors = authors,
        publisher = publisher,
        thumbnail = thumbnail,
        contents = contents,
        isbn = isbn,
        price = price,
        salePrice = salePrice,
        url = url,
        isBookmarked = isBookmarked,
    )
}

fun BookDetailItemViewState.toPresentationModel(): BookPresentationModel {
    return BookPresentationModel(
        title = title,
        authors = authors,
        publisher = publisher,
        thumbnail = thumbnail,
        contents = contents,
        isbn = isbn,
        price = price,
        salePrice = 0,
        url = url,
        isBookmarked = isBookmarked,
    )
}
