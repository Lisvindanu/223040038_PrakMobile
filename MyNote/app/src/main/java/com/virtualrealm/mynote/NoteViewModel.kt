package com.virtualrealm.mynote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualrealm.mynote.models.Note
import com.virtualrealm.mynote.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    // Get notes directly from the repository's LiveData
    val notes: LiveData<List<Note>> = noteRepository.notesLiveData

    init {
        // Load notes from network when ViewModel is created
        refreshNotes()
    }

    private fun refreshNotes() {
        viewModelScope.launch {
            noteRepository.loadItems(
                onSuccess = {
                    Log.d("NoteViewModel", "Notes refreshed successfully")
                },
                onError = { errorMsg ->
                    Log.d("NoteViewModel", "Error refreshing notes: $errorMsg")
                }
            )
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            noteRepository.insert(note,
                onSuccess = {
                    Log.d("NoteViewModel", "Insert note success")
                    // Refresh notes after insertion
                    refreshNotes()
                },
                onError = { errorMsg ->
                    Log.d("NoteViewModel", "Error inserting note: $errorMsg")
                }
            )
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.update(note.id, note,
                onSuccess = {
                    Log.d("NoteViewModel", "Update note success")
                    // Refresh notes after update
                    refreshNotes()
                },
                onError = { errorMsg ->
                    Log.d("NoteViewModel", "Error updating note: $errorMsg")
                }
            )
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.delete(note.id,
                onSuccess = {
                    Log.d("NoteViewModel", "Delete note success")
                    // Refresh notes after deletion
                    refreshNotes()
                },
                onError = { errorMsg ->
                    Log.d("NoteViewModel", "Error deleting note: $errorMsg")
                }
            )
        }
    }
}