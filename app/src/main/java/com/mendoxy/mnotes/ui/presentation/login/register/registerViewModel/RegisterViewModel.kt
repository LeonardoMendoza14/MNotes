package com.mendoxy.mnotes.ui.presentation.login.register.registerViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendoxy.mnotes.domain.useCase.LoginUseCase
import com.mendoxy.mnotes.ui.utils.LoginErrorType
import com.mendoxy.mnotes.ui.utils.LoginUiState
import com.mendoxy.mnotes.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<LoginUiState>(LoginUiState())
    val registerState: MutableStateFlow<LoginUiState> = _registerState

    private val _confirmPassword = mutableStateOf("")
    val confirmPassword = _confirmPassword


    fun setEmail(email:String){
        _registerState.update{state ->
            state.copy(email = email)
        }
    }

    fun setPassword(password:String){
        _registerState.update{state ->
            state.copy(password = password)
        }
    }

    fun setConfirmPassword(confirm: String){
        _confirmPassword.value = confirm
    }

    fun setShowPassword(){
        _registerState.update{state ->
            state.copy(showPassword = !state.showPassword)
        }
    }

    fun resetError(){
        _registerState.update { state ->
            state.copy(
                loginState = UiState.Idle
            )
        }
    }

    fun register(){
        _registerState.update{state ->
            state.copy(loginState = UiState.Loading)
        }
        viewModelScope.launch {
            val loginResult = loginUseCase.createUser(_registerState.value.email, _registerState.value.password, _confirmPassword.value)
            when(loginResult){
                LoginErrorType.NONE -> {
                    _registerState.update { state ->
                        state.copy(loginState = UiState.Success(Unit))
                    }
                }
                else -> {
                    _registerState.update { state ->
                        state.copy(loginState = UiState.Error(loginResult))
                    }
                }
            }
        }
    }
}