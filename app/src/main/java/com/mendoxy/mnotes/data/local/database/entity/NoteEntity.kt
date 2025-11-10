package com.mendoxy.mnotes.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mendoxy.mnotes.domain.model.NoteModel
import java.util.UUID

@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey() val id: String,
    val userId: String,
    val title: String,
    val content: String,
    val date: Long,
    val isDeleted: Boolean = false,
    val isPinned: Boolean = false,
    val isSync: Boolean = false
)

fun NoteEntity.toModel() = NoteModel(
    author = userId,
    idNote = id,
    title = title,
    content = content,
    date = date,
    isDeleted = isDeleted,
    isPinned = isPinned,
    isSync = isSync,
    )

fun NoteEntity.toHashMap(): HashMap<String, Any>{
    return hashMapOf(
        "idNote" to id,
        "title" to title,
        "content" to content,
        "date" to date,
        "isPinned" to isPinned,
    )
}