package com.mendoxy.mnotes.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.mendoxy.mnotes.data.local.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    // Get all notes by User
    @Query("SELECT * FROM notes_table WHERE userId = :userId ORDER BY date DESC")
    fun getAllNotesDesc(userId: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes_table WHERE userId = :userId ORDER BY date ASC")
    fun getAllNotesAsc(userId: String): Flow<List<NoteEntity>>


    // Get note for user by ID
    @Query("SELECT * FROM notes_table WHERE id = :id AND userId = :userId")
    suspend fun getNoteById(id: String, userId: String): NoteEntity?

    // Create note for user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNote(note: NoteEntity)

    // Delete note for user by id
    @Query("DELETE FROM notes_table WHERE id = :id AND userId = :userId")
    suspend fun deleteNoteById(id: String, userId: String)

    // Update note for user by id
    @Update
    suspend fun updateNote(note: NoteEntity)

    // Create or update for sync
    @Upsert
    suspend fun upsertNote(note: NoteEntity)

}