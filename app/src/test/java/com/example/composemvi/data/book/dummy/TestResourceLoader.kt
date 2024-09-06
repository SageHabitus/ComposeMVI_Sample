package com.example.composemvi.data.book.dummy

import kotlinx.serialization.json.Json
import java.io.InputStream

object TestResourceLoader {

    const val BOOK_LOCAL_TEST_JSON = "dummy_books_local.json"
    const val BOOK_REMOTE_TEST_JSON = "dummy_books_remote.json"

    fun getJsonStringFromResource(fileName: String): String {
        val classLoader = Thread.currentThread().contextClassLoader
        val inputStream: InputStream? = classLoader?.getResourceAsStream(fileName)
        return inputStream?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalArgumentException("File not found: $fileName")
    }

    inline fun <reified T> getListFromResource(fileName: String): List<T> {
        val jsonString = getJsonStringFromResource(fileName)
        return Json { ignoreUnknownKeys = true }.decodeFromString<List<T>>(jsonString)
    }
}
