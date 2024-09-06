package com.example.composemvi.data.mapper

import com.example.composemvi.data.model.BookDataModel
import com.example.composemvi.data.source.remote.model.DocumentResponseModel

fun DocumentResponseModel.toDataModel(): BookDataModel {
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
        isBookmarked = false,
    )
}
