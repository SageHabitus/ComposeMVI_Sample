package com.example.composemvi.presentation.mapper

import com.example.composemvi.domain.model.BookDomainModel
import com.example.composemvi.presentation.model.BookPresentationModel

fun BookPresentationModel.toDomainModel(): BookDomainModel {
    return BookDomainModel(
        title = title,
        authors = authors,
        contents = contents,
        isbn = isbn,
        publisher = publisher,
        price = price,
        salePrice = salePrice,
        thumbnail = thumbnail,
        url = url,
        isBookmarked = isBookmarked,
    )
}
