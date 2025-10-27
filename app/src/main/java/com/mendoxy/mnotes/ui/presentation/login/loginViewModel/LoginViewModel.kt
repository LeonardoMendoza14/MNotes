package com.mendoxy.mnotes.ui.presentation.login.loginViewModel

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.mendoxy.mnotes.R
import com.mendoxy.mnotes.domain.useCase.LoginUseCase
import com.mendoxy.mnotes.ui.utils.LoginErrorType
import com.mendoxy.mnotes.ui.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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

        // Email validation
        if(_loginState.value.email.isNotEmpty() && !loginUseCase.isValidEmail(_loginState.value.email)){
            _loginState.update { state ->
                state.copy(loginState = UIState.Error(error = LoginErrorType.INVALID_EMAIL))
            }
            return
        }else{
            _loginState.update { state ->
                state.copy(loginState = UIState.Idle)
            }
        }

        // password validation
        if(_loginState.value.password.isNotEmpty() && !loginUseCase.isValidPassword(_loginState.value.password)){
            _loginState.update { state ->
                state.copy(loginState = UIState.Error(error = LoginErrorType.INVALID_PASSWORD))
            }
            return
        }else{
            _loginState.update { state ->
                state.copy(loginState = UIState.Idle)
            }
        }

        // Login

        /*
        Implementar lo que ya tengo del viewModel en el loginScreen
        Hacer el mensaje de error en el loginScreen
        integrar el repository para el login con contraseña y correo, asi como validar si ya esta iniciada la sesion
        Agregar la validacion de sesion en mainActivity para saltarse el login
        Agregar la logica al useCase para iniciar sesion con correo y contraseña
        Agregar validaciones al viewModel con respuestas
         */



    }

}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val loginState: UIState = UIState.Idle
)