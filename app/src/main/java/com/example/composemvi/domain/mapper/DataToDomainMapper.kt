package com.example.composemvi.domain.mapper

import com.example.composemvi.data.model.BookDataModel
import com.example.composemvi.domain.model.BookDomainModel

fun BookDataModel.toDomainModel(): BookDomainModel {
    return BookDomainModel(
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
