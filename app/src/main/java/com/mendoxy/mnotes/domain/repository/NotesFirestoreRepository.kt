package com.mendoxy.mnotes.domain.repository

import com.mendoxy.mnotes.domain.model.NoteModel

interface NotesFirestoreRepository {

    suspend fun getAllNotes(userId: String): Result<List<NoteModel>>

    suspend fun createNote(userId: String, note: NoteModel): Result<Unit>

    suspend fun updateNote(userId: String, note: NoteModel): Result<Unit>

    suspend fun deleteNote(userId: String, noteId: String): Result<Unit>

}