package com.mendoxy.mnotes.domain.useCase

import android.provider.ContactsContract
import com.mendoxy.mnotes.data.remote.firebase.repository.FirebaseAuthRepositoryImpl
import com.mendoxy.mnotes.domain.model.NoteModel
import com.mendoxy.mnotes.domain.repository.NotesFirestoreRepository
import com.mendoxy.mnotes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class NotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val notesFirestoreRepository: NotesFirestoreRepository,
    private val firebaseRepository: FirebaseAuthRepositoryImpl
) {

    suspend fun syncPendingNotes(userId: String) {
        val localNotes = notesRepository.getAllNotes(userId, asc = false)
            .firstOrNull() ?: emptyList()

        localNotes.filter { !it.isSync && !it.isDeleted }.forEach { note ->
            val result = notesFirestoreRepository.updateNote(userId, note)
            if (result.isSuccess) {
                notesRepository.updateNote(note.copy(isSync = true))
            }
        }
        localNotes.filter { it.isDeleted }.forEach { note ->
            val result = notesFirestoreRepository.deleteNote(userId, note.idNote)
            if (result.isSuccess) {
                notesRepository.deleteNote(userId, note.idNote)
            }
        }

        val remoteNotes = notesFirestoreRepository.getAllNotes(userId)
        if (remoteNotes.isSuccess) {
            val remoteNotesList = remoteNotes.getOrDefault(emptyList())
            remoteNotesList.forEach { note ->
                notesRepository.upsertNote(note)
            }
        }

    }


    fun getAllNotes(userId: String, asc: Boolean) = notesRepository.getAllNotes(userId, asc)

    suspend fun syncNote(note: NoteModel){
        val result = notesFirestoreRepository.updateNote(userId = note.author, note = note)
        if(result.isSuccess){
            notesRepository.updateNote(
                note.copy(
                    isSync = true
                )
            )
        }

    }


    suspend fun createNote(userId: String, title: String, content: String){
        val note = NoteModel(
            author = userId,
            title = title,
            content = content,
        )
        notesRepository.createNote(note)

        val result = notesFirestoreRepository.createNote(userId, note)

        if (result.isSuccess) {
            notesRepository.updateNote(note.copy(isSync = true))
        }
    }

    suspend fun deleNote(userId: String, noteId: String, note: NoteModel) {
        notesRepository.updateNote(
            note.copy(
                isDeleted = true,
                isSync = false
            )
        )

        val result = notesFirestoreRepository.deleteNote(userId = userId, noteId = noteId)

        if(result.isSuccess){
            notesRepository.deleteNote(userId = userId, noteId = noteId)
        }


    }

    suspend fun updateNote(note: NoteModel){
        notesRepository.updateNote(
            note.copy(
                isSync = false
            )
        )

        val result = notesFirestoreRepository.updateNote(userId = note.author, note = note)

        if(result.isSuccess){
            notesRepository.updateNote(
                note.copy(
                    isSync = true
                )
            )
        }

    }

    fun isUserLoggedIn(): Boolean{
        return firebaseRepository.isUserLoggedIn()
    }

    fun logOut(){
        firebaseRepository.logOut()
    }

}