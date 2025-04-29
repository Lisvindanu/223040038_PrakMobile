package com.virtualrealm.mynote.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.virtualrealm.mynote.models.Note

@Dao
interface NoteDao {
    @Query("Select * From notes ORDER BY createdAt DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}