package com.example.composemvi.domain.model

data class BookDomainModel(
    val isbn: String,
    val title: String,
    val authors: List<String>,
    val publisher: String,
    val thumbnail: String,
    val contents: String,
    val price: Int,
    val salePrice: Int,
    val url: String,
    val isBookmarked: Boolean,
)
