package com.mendoxy.mnotes.ui.utils

sealed class UiState {
    data object Idle: UiState()
    data object Loading: UiState()
    data class Error(val error: Any): UiState()
    data class Success(val message: Any): UiState()
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val loginState: UiState = UiState.Idle
)

enum class LoginErrorType{
    INVALID_EMAIL,
    INVALID_PASSWORD,
    PASSWORD_NOT_MATCH,
    INVALID_lOGIN,
    NONE
}

enum class AppTheme{
    LIGHT,
    DARK
}

enum class AppFontSize(val scaleFactor: Float){
    SMALL(0.85f),
    MIDDLE(1.0f),
    BIG(1.15f)
}

enum class SortOrder{
    DESCENDING,
    ASCENDING
}