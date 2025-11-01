package com.mendoxy.mnotes.ui.presentation.login.loginViewModel

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mendoxy.mnotes.R
import com.mendoxy.mnotes.domain.useCase.LoginUseCase
import com.mendoxy.mnotes.ui.utils.LoginErrorType
import com.mendoxy.mnotes.ui.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState())
    val loginState: MutableStateFlow<LoginUiState> = _loginState


    fun setEmail(email:String){
        _loginState.update{state ->
            state.copy(email = email)
        }
    }

    fun setPassword(password:String){
        _loginState.update{state ->
            state.copy(password = password)
        }
    }

    fun setShowPassword(){
        _loginState.update{state ->
            state.copy(showPassword = !state.showPassword)
        }
    }

    fun resetError(){
        _loginState.update { state ->
            state.copy(
                loginState = UIState.Idle
            )
        }
    }

    fun login(){
        _loginState.update{state ->
            state.copy(loginState = UIState.Loading)
        }
        viewModelScope.launch {
            val loginResult = loginUseCase.login(_loginState.value.email, _loginState.value.password)
            when(loginResult){
                LoginErrorType.NONE -> {
                    _loginState.update { state ->
                        state.copy(loginState = UIState.Success(Unit))
                     }
                }
                else -> {
                    _loginState.update { state ->
                        state.copy(loginState = UIState.Error(loginResult))
                    }
                }
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.update {state ->
                state.copy(loginState = UIState.Loading)
            }

            val result = loginUseCase.loginWithGoogle(idToken)
            when(result){
                LoginErrorType.NONE -> {
                    _loginState.update { state ->
                        state.copy(loginState = UIState.Success(Unit))
                    }
                }
                else -> {
                    _loginState.update { state ->
                        state.copy(loginState = UIState.Error(result))
                    }
                }
            }
        }
    }

}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val loginState: UIState = UIState.Idle
)