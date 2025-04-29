package com.virtualrealm.mynote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.benasher44.uuid.uuid4
import com.virtualrealm.mynote.models.Note
import com.virtualrealm.mynote.utils.DateUtil
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dao = NoteDatabase.getDatabase(context).noteDao()
    val list: LiveData<List<Note>> = dao.getAllNotes()
    val notes: List<Note> by list.observeAsState(initial = listOf())

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // State untuk dialog
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    Column(modifier = modifier.padding(16.dp)) {
        // Form untuk menambahkan note
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    scope.launch {
                        try {
                            val noteId = uuid4().toString()
                            val currentDate = DateUtil.getCurrentJakartaDateTime()
                            dao.insertNote(Note(noteId, title, description, currentDate))
                            title = ""
                            description = ""
                        } catch (e: Exception) {
                            // Handle error - dalam aplikasi lengkap Anda bisa menampilkan pesan kesalahan
                            e.printStackTrace()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Note")
        }
        Spacer(Modifier.height(16.dp))

        // Daftar note
        LazyColumn {
            items(notes) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    onClick = {
                        selectedNote = note
                    },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = note.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Created: ${note.createdAt}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }

    // Tampilkan dialog jika note dipilih
    selectedNote?.let { note ->
        NoteActionDialog(
            note = note,
            onDismiss = { selectedNote = null },
            onDelete = {
                scope.launch {
                    try {
                        dao.deleteNote(note)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    selectedNote = null
                }
            },
            onEdit = { newTitle, newDescription ->
                scope.launch {
                    try {
                        val updatedNote = note.copy(
                            title = newTitle,
                            description = newDescription
                        )
                        dao.updateNote(updatedNote)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        )
    }
}