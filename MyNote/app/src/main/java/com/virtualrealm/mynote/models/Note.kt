package com.virtualrealm.mynote.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: String = "", // Default empty string for safety
    val title: String = "", // Default empty string for safety
    val description: String = "", // Default empty string for safety
    val createdAt: String = "" // Default empty string for safety
)