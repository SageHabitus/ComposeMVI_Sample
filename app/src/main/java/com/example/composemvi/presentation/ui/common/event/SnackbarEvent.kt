package com.example.composemvi.presentation.ui.common.event

import com.example.composemvi.core.mvi.Event

sealed interface SnackbarEvent : Event {
    data class Show(val message: String? = null) : SnackbarEvent
}
