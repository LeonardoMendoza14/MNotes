package com.mendoxy.mnotes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mendoxy.mnotes.data.remote.firebase.repository.SettingsRepositoryImpl
import com.mendoxy.mnotes.data.remote.firebase.repository.FirebaseAuthRepositoryImpl
import com.mendoxy.mnotes.data.remote.firebase.repository.NotesFirestoreRepositoryImpl
import com.mendoxy.mnotes.domain.repository.FirebaseAuthRepository
import com.mendoxy.mnotes.domain.repository.NotesFirestoreRepository
import com.mendoxy.mnotes.domain.repository.SettingsRepository
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

    @Provides
    @Singleton
    fun providerNotesFirestoreRepository(db: FirebaseFirestore): NotesFirestoreRepository {
        return NotesFirestoreRepositoryImpl(db = db)
    }

    @Provides
    @Singleton
    fun provderSettingsRepository(db: FirebaseFirestore): SettingsRepository {
        return SettingsRepositoryImpl(db = db)
    }

}