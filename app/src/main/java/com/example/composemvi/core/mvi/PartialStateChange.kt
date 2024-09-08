package com.example.composemvi.core.mvi

interface PartialStateChange<STATE> {
    fun reduce(oldState: STATE): STATE

    interface Success<STATE> : PartialStateChange<STATE> {
        val newValue: STATE
        override fun reduce(oldState: STATE): STATE {
            return updateState(oldState, newValue)
        }

        fun updateState(oldState: STATE, newValue: STATE): STATE
    }

    interface Failed<STATE> : PartialStateChange<STATE> {
        val errorMessage: String?
        override fun reduce(oldState: STATE): STATE {
            return updateStateWithError(oldState, errorMessage)
        }

        fun updateStateWithError(oldState: STATE, errorMessage: String?): STATE
    }

    interface Loading<STATE> : PartialStateChange<STATE> {
        override fun reduce(oldState: STATE): STATE {
            return updateStateAsLoading(oldState)
        }

        fun updateStateAsLoading(oldState: STATE): STATE
    }
}
