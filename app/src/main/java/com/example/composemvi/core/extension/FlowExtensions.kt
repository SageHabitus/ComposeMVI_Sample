package com.example.composemvi.core.extension

import android.util.Log
import com.orhanobut.logger.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.catchMap(transform: FlowCollector<T>.(Throwable) -> T): Flow<T> = catch {
    it.printStackTrace()
    emit(transform(it))
}

fun <T> concat(flow1: Flow<T>, flow2: Flow<T>): Flow<T> = flow {
    emitAll(flow1)
    emitAll(flow2)
}

fun <T> Flow<T>.startWith(item: T): Flow<T> = concat(flowOf(item), this)

fun <T> Flow<T>.endWith(item: T): Flow<T> = concat(this, flowOf(item))

fun <T> T.asFlow(): Flow<T> = flow {
    emit(this@asFlow)
}

fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = channelFlow {
    var lastEmissionTime = 0L

    collect { value ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime >= windowDuration) {
            lastEmissionTime = currentTime
            send(value)
        }
    }
}

fun <T> Flow<T>.debugLog(subject: String?): Flow<T> = if (BuildConfig.DEBUG) {
    onEach { Log.i(subject, it.toString()) }
} else {
    this
}
