package com.example.composemvi.data.mapper

import com.example.composemvi.data.source.local.entity.BookEntity
import com.example.composemvi.data.source.remote.model.DocumentResponseModel

fun DocumentResponseModel.toEntityModel(): BookEntity {
    return BookEntity(
        isbn = isbn,
        title = title,
        authors = authors,
        publisher = publisher,
        thumbnail = thumbnail,
        contents = contents,
        price = price,
        salePrice = salePrice,
        url = url,
        isBookmarked = false,
    )
}
