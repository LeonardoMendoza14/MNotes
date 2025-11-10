package com.mendoxy.mnotes.domain.repository

import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthRepository {
    fun isUserLogged(): Boolean
    suspend fun loginWithEmail(email: String, password: String): Result<Unit>
    suspend fun registerWithEmail(email: String, password: String): Result<Unit>
    suspend fun loginWithGoogle(idToken: String): Result<Unit>
    fun isUserLoggedIn(): Boolean
    fun logOut(): Result<Unit>
}