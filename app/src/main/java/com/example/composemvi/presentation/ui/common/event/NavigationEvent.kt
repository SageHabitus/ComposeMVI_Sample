package com.example.composemvi.presentation.ui.common.event

import com.example.composemvi.core.mvi.Event

sealed interface NavigationEvent<T> : Event {
    data class Success<T>(val data: T) : NavigationEvent<T>
    data class Fail(val message: String? = null) : NavigationEvent<Nothing>
}
