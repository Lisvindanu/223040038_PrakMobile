package com.virtualrealm.mynote

import androidx.room.Database
import androidx.room.RoomDatabase
import com.virtualrealm.mynote.dao.NoteDao
import com.virtualrealm.mynote.models.Note

@Database(entities = [Note::class], version = 1, exportSchema = true)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}