package com.mendoxy.mnotes.data.remote.firebase.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mendoxy.mnotes.domain.repository.FirebaseAuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthRepository {

    override fun isUserLogged(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<Unit> {
        return try{
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        }catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}