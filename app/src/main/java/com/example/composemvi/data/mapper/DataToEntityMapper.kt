package com.example.composemvi.data.mapper

import com.example.composemvi.data.model.BookDataModel
import com.example.composemvi.data.source.local.entity.BookEntity

fun BookDataModel.toEntityModel(): BookEntity {
    return BookEntity(
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
