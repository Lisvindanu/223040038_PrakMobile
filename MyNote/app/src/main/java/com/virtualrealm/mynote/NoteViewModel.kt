package com.virtualrealm.mynote

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualrealm.mynote.dao.NoteDao
import com.virtualrealm.mynote.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteDao: NoteDao) : ViewModel() {
    val notes: LiveData<List<Note>> = noteDao.getAllNotes()

    fun insertNote(note: Note) {
        viewModelScope.launch {
            noteDao.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteDao.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }
}