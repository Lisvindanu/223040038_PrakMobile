package com.virtualrealm.mynote.networks

import com.virtualrealm.mynote.models.Note

data class NoteListResponse (
    val message : String,
    val data : List<Note>
)