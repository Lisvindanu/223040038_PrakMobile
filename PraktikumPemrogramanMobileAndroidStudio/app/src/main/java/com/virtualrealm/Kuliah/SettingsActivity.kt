package com.virtualrealm.Kuliah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.virtualrealm.Kuliah.ui.theme.KuliahTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KuliahTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var isNotificationEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Pengaturan", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle tema terang/gelap
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Mode Gelap")
            Switch(checked = isDarkTheme, onCheckedChange = { isDarkTheme = it })
        }

        // Toggle notifikasi
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Notifikasi")
            Switch(checked = isNotificationEnabled, onCheckedChange = { isNotificationEnabled = it })
        }

        Button(
            onClick = { /* Tambahkan navigasi ke halaman bahasa */ },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Bahasa")
        }

        Button(
            onClick = { /* Tambahkan navigasi ke halaman tentang aplikasi */ },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Tentang Aplikasi")
        }
    }
}
