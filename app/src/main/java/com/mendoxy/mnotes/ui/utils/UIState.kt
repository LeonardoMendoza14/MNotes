package com.mendoxy.mnotes.ui.utils

sealed class UIState {
    data object Idle: UIState()
    data object Loading: UIState()
    data class Error(val error: Any): UIState()
    data class Success(val error: Any): UIState()
}

enum class LoginErrorType{
    INVALID_EMAIL,
    INVALID_PASSWORD,
    NONE
}