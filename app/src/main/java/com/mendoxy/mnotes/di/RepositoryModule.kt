package com.mendoxy.mnotes.di

import com.google.firebase.auth.FirebaseAuth
import com.mendoxy.mnotes.data.remote.firebase.repository.FirebaseAuthRepositoryImpl
import com.mendoxy.mnotes.domain.repository.FirebaseAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providerFirebaseRepository(firebaseAuth: FirebaseAuth): FirebaseAuthRepository {
        return FirebaseAuthRepositoryImpl(firebaseAuth)
    }
}