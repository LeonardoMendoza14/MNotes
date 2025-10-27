package com.mendoxy.mnotes.domain.useCase

import android.util.Patterns
import androidx.compose.runtime.Composable
import javax.inject.Inject

class LoginUseCase @Inject constructor(){

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 && password.length <= 20
    }

    fun isNotEmpty(value: String): Boolean {
        return value.trim().isNotEmpty()
    }


}