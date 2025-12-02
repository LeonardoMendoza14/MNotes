package com.mendoxy.mnotes.domain.model

import com.mendoxy.mnotes.data.remote.firebase.entity.NoteEntity
import java.util.UUID


data class NoteModel(
    val author: String = "",
    val idNote: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val date: Long = System.currentTimeMillis(),
    val pinned: Boolean = false
    )


fun NoteModel.toEntity(): NoteEntity{
    return NoteEntity(
        author = author,
        idNote = idNote,
        title = title,
        content = content,
        date = date,
        pinned = pinned,
    )
}