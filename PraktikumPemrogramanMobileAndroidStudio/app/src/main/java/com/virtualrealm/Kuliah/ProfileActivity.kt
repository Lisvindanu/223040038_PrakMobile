package com.virtualrealm.Kuliah

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualrealm.Kuliah.data.AppDatabase
import com.virtualrealm.Kuliah.data.User
import com.virtualrealm.Kuliah.ui.theme.KuliahTheme
import kotlinx.coroutines.launch

class ProfileActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val username = intent.getStringExtra("USERNAME") ?: ""
        val nama = intent.getStringExtra("NAMA") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""
        val alamat = intent.getStringExtra("ALAMAT") ?: ""
        val nomorTelepon = intent.getStringExtra("NOMOR_TELEPON") ?: ""

        setContent {
            KuliahTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Profil Pengguna") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, "Kembali")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    ProfileScreen(
                        username = username,
                        nama = nama,
                        email = email,
                        alamat = alamat,
                        nomorTelepon = nomorTelepon,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    username: String,
    nama: String,
    email: String,
    alamat: String,
    nomorTelepon: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = AppDatabase.getDatabase(context)
    val userDao = database.userDao()

    var isEditing by remember { mutableStateOf(false) }
    var editedNama by remember { mutableStateOf(nama) }
    var editedEmail by remember { mutableStateOf(email) }
    var editedAlamat by remember { mutableStateOf(alamat) }
    var editedNomorTelepon by remember { mutableStateOf(nomorTelepon) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Header
        Text(
            text = nama,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = username,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Profile Fields
        if (isEditing) {
            // Edit mode
            ProfileEditField("Nama", editedNama) { editedNama = it }
            ProfileEditField("Email", editedEmail) { editedEmail = it }
            ProfileEditField("Nomor Telepon", editedNomorTelepon) { editedNomorTelepon = it }
            ProfileEditField("Alamat", editedAlamat) { editedAlamat = it }

            // Save and Cancel buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { isEditing = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Batal")
                }

                Button(
                    onClick = {
                        scope.launch {
                            val updatedUser = User(
                                username = username,
                                password = "", // We don't know the password here, it will be preserved in Room
                                nama = editedNama,
                                nomorTelepon = editedNomorTelepon,
                                email = editedEmail,
                                alamat = editedAlamat
                            )
                            userDao.updateUserProfile(updatedUser)
                            Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                            isEditing = false
                        }
                    }
                ) {
                    Text("Simpan")
                }
            }
        } else {
            // View mode
            ProfileInfoField("Username", username)
            ProfileInfoField("Nama", nama)
            ProfileInfoField("Email", email)
            ProfileInfoField("Nomor Telepon", nomorTelepon)
            ProfileInfoField("Alamat", alamat)

            // Edit button
            Button(
                onClick = { isEditing = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text("Edit Profil")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Change password button
            OutlinedButton(
                onClick = { /* Navigate to change password screen */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ubah Password")
            }
        }
    }
}

@Composable
fun ProfileInfoField(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun ProfileEditField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            singleLine = true
        )
    }
}