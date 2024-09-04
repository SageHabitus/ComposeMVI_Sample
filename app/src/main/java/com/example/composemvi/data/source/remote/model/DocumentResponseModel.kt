package com.example.composemvi.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentResponseModel(

    @SerialName("title")
    val title: String,

    @SerialName("authors")
    val authors: List<String>,

    @SerialName("publisher")
    val publisher: String,

    @SerialName("thumbnail")
    val thumbnail: String,

    @SerialName("contents")
    val contents: String,

    @SerialName("isbn")
    val isbn: String,

    @SerialName("price")
    val price: Int,

    @SerialName("sale_price")
    val salePrice: Int,

    @SerialName("url")
    val url: String,

    @SerialName("datetime")
    val datetime: String,

    @SerialName("status")
    val status: String,

    @SerialName("translators")
    val translators: List<String>,
)
