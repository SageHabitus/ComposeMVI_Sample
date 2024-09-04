package com.example.composemvi.data.source.remote.api

import com.example.composemvi.data.source.remote.model.BookResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {

    @GET("v3/search/book")
    suspend fun searchBooks(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): BookResponseModel
}
