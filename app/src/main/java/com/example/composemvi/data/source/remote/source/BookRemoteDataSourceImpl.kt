package com.example.composemvi.data.source.remote.source

import com.example.composemvi.data.source.remote.api.BookApi
import com.example.composemvi.data.source.remote.model.BookResponseModel
import javax.inject.Inject

class BookRemoteDataSourceImpl @Inject constructor(
    private val api: BookApi,
) : BookRemoteDataSource {

    override suspend fun searchBooks(query: String, page: Int, size: Int): BookResponseModel {
        return api.searchBooks(query, page, size)
    }
}
