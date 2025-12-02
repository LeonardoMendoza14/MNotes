package com.mendoxy.mnotes.domain.useCase

import com.google.firebase.firestore.Query.Direction
import com.mendoxy.mnotes.domain.model.NoteModel
import com.mendoxy.mnotes.domain.repository.NotesFirestoreRepository
import com.mendoxy.mnotes.ui.utils.SortOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesUseCase @Inject constructor(
    private val notesRepository: NotesFirestoreRepository
) {

    fun getAllNotes(userId: String ): Flow<List<NoteModel>> {
        val notes = notesRepository.getAllNotes(userId = userId)

        return notes
    }

    suspend fun createNote(userId: String, title: String, content: String){
        val note = NoteModel(
            author = userId,
            title = title,
            content = content,
        )
        notesRepository.createNote(userId = userId, note = note)
    }

    suspend fun deleteNote(userId: String, noteId: String) {
        notesRepository.deleteNote(userId = userId, noteId = noteId)
    }

    suspend fun updateNote(userId: String, note: NoteModel){
        notesRepository.updateNote(userId = userId, note = note)
    }

}