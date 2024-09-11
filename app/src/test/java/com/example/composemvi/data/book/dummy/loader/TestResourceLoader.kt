package com.example.composemvi.data.book.dummy.loader

import kotlinx.serialization.json.Json
import java.io.InputStream

object TestResourceLoader {

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
