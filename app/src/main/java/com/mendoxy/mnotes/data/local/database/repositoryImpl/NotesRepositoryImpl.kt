package com.mendoxy.mnotes.data.local.database.repositoryImpl

import com.mendoxy.mnotes.data.local.database.dao.NotesDao
import com.mendoxy.mnotes.data.local.database.entity.toModel
import com.mendoxy.mnotes.domain.model.NoteModel
import com.mendoxy.mnotes.domain.model.toEntity
import com.mendoxy.mnotes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao
): NotesRepository {
    override suspend fun createNote(note: NoteModel): Result<Boolean> {
        return try{
            val noteEntity = note.toEntity()
            notesDao.createNote(noteEntity)
            Result.success(true)

        }catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun updateNote(note: NoteModel): Result<Boolean> {
        return try{
            val noteEntity = note.toEntity()
            notesDao.updateNote(noteEntity)
            Result.success(true)
        }catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteNote(
        noteId: String,
        userId: String
    ): Result<Boolean> {
        return try{
            notesDao.deleteNoteById(noteId, userId)
            Result.success(true)
        }catch(e: Exception){
            Result.failure(e)
        }
    }

    override fun getAllNotes(userId: String, asc: Boolean): Flow<List<NoteModel>> {
        return if(asc){
            notesDao.getAllNotesAsc(userId).map{notes ->
                notes.map{it.toModel()}
            }
        }else{
            notesDao.getAllNotesDesc(userId).map{notes ->
                notes.map{it.toModel()}
            }
        }
    }

    override suspend fun getNoteById(
        noteId: String,
        userId: String
    ): NoteModel {
        val note = notesDao.getNoteById(noteId, userId)
        return note?.toModel() ?: NoteModel()
    }

    override suspend fun upsertNote(note: NoteModel): Result<Boolean> {
        return try{
            val noteEntity = note.toEntity()
            notesDao.upsertNote(noteEntity)
            Result.success(true)
        }catch(e: Exception){
            Result.failure(e)
        }
    }
}