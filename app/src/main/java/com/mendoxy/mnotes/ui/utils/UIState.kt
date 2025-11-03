package com.mendoxy.mnotes.ui.utils

sealed class UIState {
    data object Idle: UIState()
    data object Loading: UIState()
    data class Error(val error: Any): UIState()
    data class Success(val message: Any): UIState()
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val loginState: UIState = UIState.Idle
)

enum class LoginErrorType{
    INVALID_EMAIL,
    INVALID_PASSWORD,
    PASSWORD_NOT_MATCH,
    INVALID_lOGIN,
    NONE
}