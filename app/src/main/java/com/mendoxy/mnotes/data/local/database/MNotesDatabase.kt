package com.mendoxy.mnotes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mendoxy.mnotes.data.local.database.dao.NotesDao
import com.mendoxy.mnotes.data.local.database.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class MNotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

}
