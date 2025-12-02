package com.mendoxy.mnotes.data.remote.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.SetOptions
import com.mendoxy.mnotes.data.remote.firebase.routes.FirestoreRoutes
import com.mendoxy.mnotes.domain.model.NoteModel
import com.mendoxy.mnotes.domain.model.toEntity
import com.mendoxy.mnotes.domain.repository.NotesFirestoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesFirestoreRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): NotesFirestoreRepository {

    override fun getAllNotes(userId: String): Flow<List<NoteModel>> {
        return callbackFlow{
            val ref = db.collection(FirestoreRoutes.COLLECTION_USERS)
                .document(userId)
                .collection(FirestoreRoutes.COLLECTION_NOTES)

            val listener = ref.addSnapshotListener { snapshot, error ->
                if(error != null){
                    close(error)
                    return@addSnapshotListener
                }
                if(snapshot != null && !snapshot.isEmpty()){
                    try {
                        // Truco Pro: mapNotNull evita que la app crashee si un documento está corrupto
                        val notes = snapshot.documents.mapNotNull { doc ->
                            // Convertimos el documento JSON a tu Data Class
                            doc.toObject(NoteModel::class.java)?.copy(
                                // Opcional: Aseguramos que el ID del modelo sea el del documento
                                idNote = doc.id
                            )
                        }
                        trySend(notes)
                    }catch (e: Exception){
                        trySend(emptyList())
                    }
                }else {
                    trySend(emptyList())
                }
            }
            // Cerrar el listener cuando el Flow se cancele
            awaitClose {
                listener.remove()
            }

        }
    }

    override suspend fun createNote(
        userId: String,
        note: NoteModel
    ) {
        try {
            db.collection(FirestoreRoutes.COLLECTION_USERS)
                .document(userId)
                .collection(FirestoreRoutes.COLLECTION_NOTES)
                .document(note.idNote)
                .set(note.toEntity()).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateNote(
        userId: String,
        note: NoteModel
    ) {
        try{
            db.collection(FirestoreRoutes.COLLECTION_USERS)
                .document(userId)
                .collection(FirestoreRoutes.COLLECTION_NOTES)
                .document(note.idNote)
                .set(note.toEntity(), SetOptions.merge()).await()
        }catch(e: Exception){
            throw e
        }
    }

    override suspend fun deleteNote(
        userId: String,
        noteId: String
    ) {
        try{
            db.collection(FirestoreRoutes.COLLECTION_USERS)
                .document(userId)
                .collection(FirestoreRoutes.COLLECTION_NOTES)
                .document(noteId)
                .delete().await()
        }catch(e: Exception){
            throw e
        }
    }
}