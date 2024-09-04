package com.example.composemvi.data.source.remote.source

import com.example.composemvi.data.source.remote.model.BookResponseModel

interface BookRemoteDataSource {

    suspend fun searchBooks(query: String, page: Int, size: Int): BookResponseModel
}
