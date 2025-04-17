package com.namaya.oscarsthegrouch_app.ui

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val value: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}