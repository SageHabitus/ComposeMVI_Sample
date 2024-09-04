package com.example.composemvi.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookResponseModel(

    // 책 리스트
    @SerialName("documents")
    val documents: List<DocumentResponseModel>,

    // 페이지 관련 정보
    @SerialName("meta")
    val meta: MetaResponseModel,

)
