package com.example.composemvi.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "book", indices = [Index(value = ["isbn"], unique = true)])
@Serializable
data class BookEntity(

    @PrimaryKey
    @ColumnInfo(name = "isbn")
    val isbn: String,

    // 도서 제목
    @ColumnInfo(name = "title")
    val title: String,

    // 저자
    @ColumnInfo(name = "authors")
    val authors: List<String>,

    // 내용
    @ColumnInfo(name = "contents")
    val contents: String,

    // 출판사
    @ColumnInfo(name = "publisher")
    val publisher: String,

    @ColumnInfo(name = "price")
    val price: Int,

    // 도서 판매가
    @ColumnInfo(name = "sale_price")
    val salePrice: Int,

    // 도서 표지
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,

    // 도서 URL (고유값)
    @ColumnInfo(name = "url")
    val url: String,

    // 즐겨찾기 여부
    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean,
)
