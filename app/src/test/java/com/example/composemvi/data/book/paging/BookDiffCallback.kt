package com.example.composemvi.data.book.paging

import androidx.recyclerview.widget.DiffUtil
import com.example.composemvi.data.model.BookDataModel

class BookDiffCallback : DiffUtil.ItemCallback<BookDataModel>() {
    override fun areItemsTheSame(oldItem: BookDataModel, newItem: BookDataModel): Boolean {
        return oldItem.isbn == newItem.isbn
    }

    override fun areContentsTheSame(oldItem: BookDataModel, newItem: BookDataModel): Boolean {
        return oldItem == newItem
    }
}
