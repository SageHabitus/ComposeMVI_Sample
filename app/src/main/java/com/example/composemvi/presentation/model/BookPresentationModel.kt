package com.example.composemvi.presentation.model

data class BookPresentationModel(
    val title: String,
    val authors: List<String>,
    val publisher: String,
    val thumbnail: String,
    val contents: String,
    val isbn: String,
    val price: Int,
    val salePrice: Int,
    val url: String,
    val isBookmarked: Boolean,
)
