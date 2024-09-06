package com.example.composemvi.data.mapper

import com.example.composemvi.data.model.BookDataModel
import com.example.composemvi.data.source.local.entity.BookEntity

fun BookEntity.toDataModel(): BookDataModel {
    return BookDataModel(
        isbn = isbn,
        title = title,
        authors = authors,
        publisher = publisher,
        thumbnail = thumbnail,
        contents = contents,
        price = price,
        salePrice = salePrice,
        url = url,
        isBookmarked = isBookmarked,
    )
}
