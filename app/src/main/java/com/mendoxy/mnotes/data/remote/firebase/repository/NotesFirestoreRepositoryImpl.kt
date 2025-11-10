package com.mendoxy.mnotes.data.remote.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mendoxy.mnotes.domain.model.NoteModel
import com.mendoxy.mnotes.domain.model.toHashMap
import com.mendoxy.mnotes.domain.repository.NotesFirestoreRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotesFirestoreRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): NotesFirestoreRepository {

    override suspend fun getAllNotes(userId: String): Result<List<NoteModel>> {
        return try{
            val notes = db.collection("users")
                .document(userId)
                .collection("notes")
                .get().await()
                .documents.mapNotNull{ note ->
                    try{
                        NoteModel(
                            author = userId,
                            idNote = note.getString("idNote") ?: "",
                            title = note.getString("title") ?: "",
                            content = note.getString("content") ?: "",
                            date = note.getLong("date") ?: 0,
                            isPinned = note.getBoolean("isPinned") ?: false,
                            isSync = note.getBoolean("isSync") ?: true
                        )
                    }catch (e: Exception){
                        null
                    }
                }

            Result.success(notes)
        }catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun createNote(
        userId: String,
        note: NoteModel
    ): Result<Unit> {
        return try{
            db.collection("users")
                .document(userId)
                .collection("notes")
                .document(note.idNote)
                .set(note.toHashMap()).await()

            Result.success(Unit)
        } catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun updateNote(
        userId: String,
        note: NoteModel
    ): Result<Unit> {
        return try{
            db.collection("users")
                .document(userId)
                .collection("notes")
                .document(note.idNote)
                .set(note.toHashMap()).await()

            Result.success(Unit)
        }catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteNote(
        userId: String,
        noteId: String
    ): Result<Unit> {
        return try{
            db.collection("users")
                .document(userId)
                .collection("notes")
                .document(noteId)
                .delete().await()

            Result.success(Unit)
        }catch(e: Exception){
            Result.failure(e)
        }
    }
}