package com.virtualrealm.mynote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.virtualrealm.mynote.models.Note

@Composable
fun NoteActionDialog(
    note: Note,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (String, String) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    if (showEditDialog) {
        EditNoteDialog(
            note = note,
            onDismiss = { showEditDialog = false },
            onConfirm = { title, description ->
                onEdit(title, description)
                showEditDialog = false
                onDismiss()
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Note Actions")
            },
            text = {
                Column {
                    Text("Title: ${note.title}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Created at: ${note.createdAt}")
                }
            },
            confirmButton = {
                Row {
                    TextButton(
                        onClick = {
                            showEditDialog = true
                        }
                    ) {
                        Text("Edit")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onDelete()
                            onDismiss()
                        }
                    ) {
                        Text("Delete")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EditNoteDialog(
    note: Note,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(note.title) }
    var description by remember { mutableStateOf(note.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Note") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(title, description) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}