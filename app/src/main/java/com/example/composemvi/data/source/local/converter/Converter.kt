package com.example.composemvi.data.source.local.converter

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converter {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, String::class.java)
    private val jsonAdapter = moshi.adapter<List<String>>(listType)

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return jsonAdapter.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return jsonAdapter.fromJson(value) ?: emptyList()
    }
}
