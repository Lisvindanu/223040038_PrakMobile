package com.virtualrealm.mynote.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import com.virtualrealm.mynote.dao.NoteDao
import com.virtualrealm.mynote.models.Note
import com.virtualrealm.mynote.networks.NoteApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val api: NoteApi,
    private val dao: NoteDao
) {
    // Get notes directly as LiveData
    val notesLiveData: LiveData<List<Note>> = dao.getAllNotes()

    @WorkerThread
    fun loadItems(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ): Flow<List<Note>> = flow<List<Note>> {
        // Always emit current local data first for immediate UI display
        emit(dao.getAllNotes().value ?: emptyList())

        // Then try to fetch from network
        try {
            api.all()
                .suspendOnSuccess {
                    data.whatIfNotNull { response ->
                        // If API call is successful, update the local database
                        response.data.let { notes ->
                            try {
                                // Insert each note individually to avoid bulk issues
                                notes.forEach { note ->
                                    // Ensure no fields are null before inserting
                                    if (note.id.isNotBlank() &&
                                        note.title.isNotBlank() &&
                                        note.createdAt.isNotBlank()) {
                                        dao.insertNote(note)
                                    }
                                }
                                emit(dao.getAllNotes().value ?: emptyList())
                                onSuccess()
                            } catch (e: Exception) {
                                Log.e("NoteRepository", "Error inserting notes: ${e.message}")
                                onError("Error inserting notes: ${e.message}")
                            }
                        }
                    }
                }
                .suspendOnError {
                    onError(message())
                }
                .suspendOnException {
                    onError(message())
                }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error loading notes: ${e.message}")
            onError("Error loading notes: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    suspend fun insert(item: Note, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            // Always save to local database first (offline-first approach)
            dao.insertNote(item)

            // Then try to sync with server
            api.insert(item)
                .suspendOnSuccess {
                    onSuccess()
                }
                .suspendOnError {
                    onError(message())
                }
                .suspendOnException {
                    onError(message())
                }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error inserting note: ${e.message}")
            onError("Error inserting note: ${e.message}")
        }
    }

    suspend fun update(id: String, item: Note, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            // Update local database first
            dao.updateNote(item)

            // Then try to sync with server
            api.update(id, item)
                .suspendOnSuccess {
                    onSuccess()
                }
                .suspendOnError {
                    onError(message())
                }
                .suspendOnException {
                    onError(message())
                }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error updating note: ${e.message}")
            onError("Error updating note: ${e.message}")
        }
    }

    suspend fun delete(id: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            // Delete from local database first
            dao.deleteNote(id)

            // Then try to sync with server
            api.delete(id)
                .suspendOnSuccess {
                    onSuccess()
                }
                .suspendOnError {
                    onError(message())
                }
                .suspendOnException {
                    onError(message())
                }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error deleting note: ${e.message}")
            onError("Error deleting note: ${e.message}")
        }
    }
}