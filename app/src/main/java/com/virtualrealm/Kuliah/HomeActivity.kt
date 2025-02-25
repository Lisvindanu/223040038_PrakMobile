package com.virtualrealm.Kuliah

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualrealm.Kuliah.data.AppDatabase
import com.virtualrealm.Kuliah.data.User
import com.virtualrealm.Kuliah.ui.theme.KuliahTheme
import com.virtualrealm.Kuliah.ui.theme.Pink40
import com.virtualrealm.Kuliah.ui.theme.Purple80
import kotlinx.coroutines.runBlocking

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get user data passed from login
        val username = intent.getStringExtra("USERNAME") ?: ""
        val nama = intent.getStringExtra("NAMA") ?: ""
        val email = intent.getStringExtra("EMAIL") ?: ""
        val alamat = intent.getStringExtra("ALAMAT") ?: ""
        val nomorTelepon = intent.getStringExtra("NOMOR_TELEPON") ?: ""

        setContent {
            KuliahTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
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
fun HomeScreen(
    username: String,
    nama: String,
    email: String = "",
    alamat: String = "",
    nomorTelepon: String = "",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selamat Datang",
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = nama,
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Username: $username",
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Add your menu or main content here
        MenuButtons(
            onLogout = {
                // Navigate back to login screen
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)

                // If activity context is available, finish it
                (context as? ComponentActivity)?.finish()
            }
        )
    }
}

@Composable
fun MenuButtons(onProfileClick: () -> Unit = {}, onLogout: () -> Unit = {}) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Purple80,
        contentColor = Color.White
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onProfileClick,
            colors = buttonColors,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(
                text = "Profil Pengguna",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(8.dp)
            )
        }


        Button(
            onClick = { /* Navigate to another feature */ },
            colors = buttonColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Fitur Lainnya",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(8.dp)
            )
        }

        Button(
            onClick = { /* Navigate to settings */ },
            colors = buttonColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Pengaturan",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor = Pink40,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Keluar",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val userDao = database.userDao()

    // For preview, we need to use a synchronous approach to get data from Room
    // We'll use runBlocking, but only for preview purposes
    val user = runBlocking {
        // Try to get the first user from database, or use empty values if none exist
        userDao.getFirstUser() ?: User(
            username = "preview_user",
            password = "password",
            nama = "Preview User",
            nomorTelepon = "081234567890",
            email = "preview@example.com",
            alamat = "Jl. Preview No. 123"
        )
    }

    KuliahTheme {
        HomeScreen(
            username = user.username,
            nama = user.nama,
            email = user.email,
            alamat = user.alamat,
            nomorTelepon = user.nomorTelepon
        )
    }
}