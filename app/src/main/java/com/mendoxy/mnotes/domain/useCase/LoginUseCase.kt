package com.mendoxy.mnotes.domain.useCase

import android.util.Patterns
import com.google.android.gms.common.api.Response
import com.mendoxy.mnotes.domain.repository.FirebaseAuthRepository
import com.mendoxy.mnotes.ui.utils.LoginErrorType
import com.mendoxy.mnotes.ui.utils.UIState
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: FirebaseAuthRepository
){

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 && password.length <= 20
    }

    fun isNotEmpty(value: String): Boolean {
        return value.trim().isNotEmpty()
    }

    suspend fun login(email: String, password: String): LoginErrorType  {
        // Email validation
        if(!isNotEmpty(email) || !isValidEmail(email)){
            return LoginErrorType.INVALID_EMAIL
        }

        // password validation
        if(!isNotEmpty(password) || !isValidPassword(password)){
            return LoginErrorType.INVALID_PASSWORD
        }

        try{

            val result = loginRepository.loginWithEmail(email, password)
            if(result.isFailure){
                return LoginErrorType.INVALID_lOGIN
            }else{
                return LoginErrorType.NONE
            }

        }catch(e: Exception){
            return LoginErrorType.INVALID_lOGIN

        }
    }

    suspend fun loginWithGoogle(idToken: String): LoginErrorType{
        return try{
            val result = loginRepository.loginWithGoogle(idToken)
            if(result.isFailure){
                return LoginErrorType.INVALID_lOGIN
            }else{
                return LoginErrorType.NONE
            }

        }catch(e: Exception){
            return LoginErrorType.INVALID_lOGIN

        }
    }


}