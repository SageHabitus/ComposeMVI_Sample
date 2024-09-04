package com.example.composemvi.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaResponseModel(

    @SerialName("total_count")
    val totalCount: Int,

    @SerialName("pageable_count")
    val pageableCount: Int,

    @SerialName("is_end")
    val isEnd: Boolean,

)
