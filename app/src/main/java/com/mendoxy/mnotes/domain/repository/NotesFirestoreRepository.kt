package com.mendoxy.mnotes.domain.repository

import com.google.firebase.firestore.Query.Direction
import com.mendoxy.mnotes.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NotesFirestoreRepository {

    fun getAllNotes(userId: String): Flow<List<NoteModel>>

    suspend fun createNote(userId: String, note: NoteModel)

    suspend fun updateNote(userId: String, note: NoteModel)

    suspend fun deleteNote(userId: String, noteId: String)

}