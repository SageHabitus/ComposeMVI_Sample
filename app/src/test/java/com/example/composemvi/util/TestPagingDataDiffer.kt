package com.example.composemvi.util

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ItemSnapshotList
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers

class TestPagingDataDiffer<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T> = DefaultDiffCallback(),
) {

    private val differ = AsyncPagingDataDiffer(
        diffCallback = diffCallback,
        updateCallback = NoopListUpdateCallback(),
        workerDispatcher = Dispatchers.Main,
    )

    suspend fun submitData(pagingData: PagingData<T>) {
        differ.submitData(pagingData)
    }

    fun snapshot(): ItemSnapshotList<T> {
        return differ.snapshot()
    }

    class DefaultDiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }

    private class NoopListUpdateCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) = Unit
        override fun onRemoved(position: Int, count: Int) = Unit
        override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
        override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
    }
}
