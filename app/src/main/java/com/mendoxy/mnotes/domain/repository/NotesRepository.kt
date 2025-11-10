package com.mendoxy.mnotes.domain.repository

import com.mendoxy.mnotes.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun createNote(note: NoteModel): Result<Boolean>

    suspend fun updateNote(note: NoteModel): Result<Boolean>

    suspend fun deleteNote(noteId: String, userId: String): Result<Boolean>

     fun getAllNotes(userId: String, asc: Boolean): Flow<List<NoteModel>>

    suspend fun getNoteById(noteId: String, userId: String): NoteModel?

    suspend fun upsertNote(note: NoteModel): Result<Boolean>


}