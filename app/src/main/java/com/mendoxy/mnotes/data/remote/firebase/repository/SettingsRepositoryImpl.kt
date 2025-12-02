package com.mendoxy.mnotes.data.remote.firebase.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.mendoxy.mnotes.data.remote.firebase.entity.SettingsEntity
import com.mendoxy.mnotes.data.remote.firebase.entity.toModel
import com.mendoxy.mnotes.data.remote.firebase.routes.FirestoreRoutes
import com.mendoxy.mnotes.domain.model.SettingsModel
import com.mendoxy.mnotes.domain.model.toEntity
import com.mendoxy.mnotes.domain.repository.SettingsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : SettingsRepository {

    override suspend fun updateSettingsForUser(
        userId: String,
        settings: SettingsModel
    ) {
        try {
            val ref = db
                .collection(FirestoreRoutes.COLLECTION_USERS)
                .document(userId)
                .collection(FirestoreRoutes.COLLECTION_SETTINGS)
                .document(FirestoreRoutes.DEFAULT_SETTINGS)

            ref.set(settings.toEntity(), SetOptions.merge()).await()

        } catch (e: Exception) {
            Log.e("ERROR", "Error al actualizar las preferencias")
            throw e
        }
    }

    override fun getSettingsByUser(userId: String): Flow<SettingsModel?> {
        return callbackFlow {
            val ref = db
                .collection(FirestoreRoutes.COLLECTION_USERS)
                .document(userId)
                .collection(FirestoreRoutes.COLLECTION_SETTINGS)
                .document(FirestoreRoutes.DEFAULT_SETTINGS)

            val listener = ref.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    try {
                        val entity = snapshot.toObject<SettingsEntity>()
                        val model = entity?.toModel()
                        trySend(model)
                    } catch (e: Exception) {
                        trySend(null)
                    }
                } else {
                    trySend(null)
                }
            }

            awaitClose {
                listener.remove()
            }
        }
    }
}