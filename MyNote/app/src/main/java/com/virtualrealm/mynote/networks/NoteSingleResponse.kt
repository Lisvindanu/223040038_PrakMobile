package com.virtualrealm.mynote.networks

import com.virtualrealm.mynote.models.Note

data class NoteSingleResponse (
    val message: String,
    val data: Note?
)