package com.mendoxy.mnotes.data.remote.firebase.entity


data class NoteEntity(
    val author: String = "",
    val idNote: String = "",
    val title: String = "",
    val content: String = "",
    val date: Long = 0L,
    val pinned: Boolean = false
)