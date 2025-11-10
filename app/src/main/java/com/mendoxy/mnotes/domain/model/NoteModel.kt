package com.mendoxy.mnotes.domain.model

import com.mendoxy.mnotes.data.local.database.entity.NoteEntity
import java.util.UUID


data class NoteModel(
    val author: String = "",
    val idNote: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val date: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val isPinned: Boolean = false,
    val isSync: Boolean = false,
    )

fun NoteModel.toHashMap(): HashMap<String, Any>{
    return hashMapOf(
        "idNote" to idNote,
        "title" to title,
        "content" to content,
        "date" to date,
        "isPinned" to isPinned,
    )
}

fun HashMap<String, Any>.toNoteModel(userId: String): NoteModel{
    return NoteModel(
        author = userId,
        idNote = get("idNote") as String,
        title = get("title") as String,
        content = get("content") as String,
        date = get("date") as Long,
        isPinned = get("isPinned") as Boolean,
        isSync = true
    )
}

fun NoteModel.toEntity(): NoteEntity{
    return NoteEntity(
        userId = author,
        id = idNote,
        title = title,
        content = content,
        date = date,
        isDeleted = isDeleted,
        isPinned = isPinned,
        isSync = isSync
    )
}